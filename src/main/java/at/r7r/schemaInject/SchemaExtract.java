package at.r7r.schemaInject;

import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.thoughtworks.xstream.XStream;

import at.r7r.schemaInject.dao.DatabaseHelper;
import at.r7r.schemaInject.dao.SqlBuilder;
import at.r7r.schemaInject.entity.Field;
import at.r7r.schemaInject.entity.ForeignKey;
import at.r7r.schemaInject.entity.PrimaryKey;
import at.r7r.schemaInject.entity.Schema;
import at.r7r.schemaInject.entity.Table;
import at.r7r.schemaInject.entity.Unique;

/**
 * Extracts entity objects based on the current database schema
 */
public class SchemaExtract {
	private Connection conn;
	private String metaTableName;
	
	public SchemaExtract(Connection conn, String metaTableName) {
		this.conn = conn;
		this.metaTableName = metaTableName;
	}
	
	public SchemaExtract(Connection conn) {
		this(conn, "_schemaInject");
	}
	
	public Schema getSchema() throws SQLException {
		// TODO get revision, metatable name and prefix
		Schema rc = new Schema();
		
		SqlBuilder query = new SqlBuilder(" ", false);
		query.append("SELECT MAX(\"revision\") FROM");
		query.appendIdentifier(metaTableName);

		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(query.join());
		int rev = 0;
		
		if (rs.next()) {
			rev = rs.getInt(1);
		}
		
		rc.setMetaTable(metaTableName);
		rc.setRevision(rev);
		rc.polish();
		
		for (String tableName: new DatabaseHelper(conn).listTables()) {
			rc.addTable(getTable(rc, tableName));
		}
		
		return rc;
	}

	private Table getTable(Schema schema, String name) throws SQLException {
		DatabaseHelper db = new DatabaseHelper(conn);
		List<Field> fields = db.getFields(name);
		PrimaryKey pkey = db.getPrimaryKey(name);
		List<ForeignKey> fkeys = db.getForeignKeys(name);
		List<Unique> uniques = db.getUniqueConstraints(name);
		return new Table(schema, name, fields, pkey, fkeys, uniques);
	}
	
	public void writeSchema(Schema schema, OutputStream os) {
		XStream xstream = SchemaInject.getXStream();
		xstream.toXML(schema, os);
	}
}
