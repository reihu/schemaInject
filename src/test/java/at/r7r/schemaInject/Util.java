package at.r7r.schemaInject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.thoughtworks.xstream.XStream;

import at.r7r.schemaInject.entity.Schema;

public class Util {
	public static Connection getDbConnection() throws SQLException {
		Connection rc = DriverManager.getConnection("jdbc:hsqldb:mem:testDb", "sa", "");
		return rc;
	}
	
	public static XStream getXStream() {
		XStream xstream = new XStream();
		xstream.processAnnotations(Schema.class);
		return xstream;
	}
	
	public static Schema readSchema(InputStream is) {
		XStream xstream = getXStream();
		Schema rc = (Schema) xstream.fromXML(is);
		rc.assignNamesToUnnamedIndices();
		return rc;
	}
	
	public static Schema readSchema(String filename) throws FileNotFoundException {
		File file = new File(filename);
		return readSchema(new FileInputStream(file));
	}
}
