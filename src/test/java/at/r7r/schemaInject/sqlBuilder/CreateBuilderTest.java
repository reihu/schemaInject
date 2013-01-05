package at.r7r.schemaInject.sqlBuilder;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Assert;

import at.r7r.schemaInject.SchemaExtract;
import at.r7r.schemaInject.SchemaInject;
import at.r7r.schemaInject.Util;
import at.r7r.schemaInject.entity.Schema;
import junit.framework.TestCase;

public class CreateBuilderTest extends TestCase {
	/**
	 * Tries to inject a schema into a HSQL in-memory database. It then re-extracts it and compares them
	 * @throws SQLException
	 */
	public void testHsqlCreate() throws SQLException {
		// read schema.xml
		SchemaInject injecter = new SchemaInject();
		InputStream is = ClassLoader.getSystemResourceAsStream("createTest/catandmouse.xml");
		Schema schema = injecter.readSchema(is);
		Connection conn = Util.getDbConnection();
		injecter.inject(conn, schema);

		// extract schema and compares the two
		SchemaExtract extracter = new SchemaExtract(conn, schema.getMetaTable());
		Schema resultingSchema = extracter.getSchema();

		System.out.println("Original schema:");
		extracter.writeSchema(schema, System.out);

		System.out.println("Resulting schema:");
		extracter.writeSchema(resultingSchema, System.out);
		
		Assert.assertEquals(schema, resultingSchema);
		
	}
}
