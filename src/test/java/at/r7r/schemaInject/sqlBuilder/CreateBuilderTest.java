package at.r7r.schemaInject.sqlBuilder;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

import com.thoughtworks.xstream.XStream;

import at.r7r.schemaInject.SchemaExtract;
import at.r7r.schemaInject.Util;
import at.r7r.schemaInject.dao.DatabaseHelper;
import at.r7r.schemaInject.entity.Schema;
import at.r7r.schemaInject.entity.Table;
import junit.framework.TestCase;

public class CreateBuilderTest extends TestCase {
	public void testBuildTable() throws SQLException {
		// read schema.xml
		InputStream is = ClassLoader.getSystemResourceAsStream("createTest/catandmouse.xml");
		Schema schema = Util.readSchema(is);
		Connection conn = Util.getDbConnection();

		// inject each table
		for (Table table: schema.getTables()) {
			new DatabaseHelper(conn).createTable(table);
		}

		// extract tables and print them
		SchemaExtract extracter = new SchemaExtract();
		Schema resultingSchema = extracter.getSchema(conn);
		XStream xstream = Util.getXStream();
		System.out.println("Resulting schema:");
		xstream.toXML(resultingSchema, System.out);
	}
}
