package at.r7r.schemaInject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
	public static Connection getDbConnection() throws SQLException {
		Connection rc = DriverManager.getConnection("jdbc:hsqldb:mem:testDb", "sa", "");
		return rc;
	}
}
