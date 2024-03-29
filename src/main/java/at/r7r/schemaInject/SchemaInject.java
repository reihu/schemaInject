package at.r7r.schemaInject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import com.thoughtworks.xstream.XStream;

import at.r7r.schemaInject.dao.DatabaseHelper;
import at.r7r.schemaInject.dao.SqlBuilder;
import at.r7r.schemaInject.entity.Column;
import at.r7r.schemaInject.entity.Datatype;
import at.r7r.schemaInject.entity.PrimaryKey;
import at.r7r.schemaInject.entity.Schema;
import at.r7r.schemaInject.entity.Table;
import at.r7r.schemaInject.xstreamConverter.DatatypeConverter;

public class SchemaInject {
	private static Table createMetaTable(String tableName) {
		List<Column> columns = new ArrayList<Column>();
		List<String> pkeyColumns = new LinkedList<String>();
		columns.add(new Column(null, "revision", new Datatype("INTEGER"), false, null));
		columns.add(new Column(null, "ts", new Datatype("TIMESTAMP"), false, null)); // not using a default value here as this is db-specific
		pkeyColumns.add("revision");
		
		PrimaryKey pkey = new PrimaryKey(null, tableName+"_pkey", pkeyColumns);
		return new Table(null, tableName, columns, pkey, null, null);
	}
	
	static XStream getXStream() {
		XStream xstream = new XStream();
		xstream.registerConverter(new DatatypeConverter());
		xstream.processAnnotations(Schema.class);
		return xstream;
	}
	
	public void inject(Connection conn, Schema schema) throws SQLException {
		DatabaseHelper dh = new DatabaseHelper(conn);

		schema.polish();
		
		// inject each table
		for (Table table: schema.getTables()) {
			dh.createTable(table);
		}

		Table metaTable = createMetaTable(schema.getMetaTable());
		dh.createTable(metaTable);
		
		SqlBuilder sql = new SqlBuilder(" ", false);
		sql.append("INSERT INTO");
		sql.appendIdentifier(schema.getMetaTable());
		sql.append("(\"revision\", \"ts\") VALUES (?,?)");
		PreparedStatement stmt = conn.prepareStatement(sql.join());
		stmt.setInt(1, schema.getRevision());
		stmt.setTimestamp(2, new Timestamp(Calendar.getInstance().getTimeInMillis()));
		stmt.execute();
	}

	public Schema readSchema(InputStream is) {
		XStream xstream = getXStream();
		Schema rc = (Schema) xstream.fromXML(is);
		rc.assignNamesToUnnamedIndices();
		return rc;

	}
	
	public Schema readSchema(String filename) throws FileNotFoundException {
		File file = new File(filename);
		return readSchema(new FileInputStream(file));
	}
}
