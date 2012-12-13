package at.r7r.schemaInject;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import at.r7r.schemaInject.dao.DatabaseHelper;
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
	private static List<ForeignKey> getForeignKeys(Connection conn, String tableName) throws SQLException {
		return new DatabaseHelper(conn).getForeignKeys(tableName);
	}

	private static PrimaryKey getPrimaryKey(Connection conn, String tableName) throws SQLException {
		return new DatabaseHelper(conn).getPrimaryKey(tableName);
	}

	public Schema getSchema(Connection conn) throws SQLException {
		// TODO get revision, metatable name and prefix
		Schema rc = new Schema();
		for (String tableName: new DatabaseHelper(conn).listTables()) {
			rc.addTable(getTable(conn, tableName));
		}
		return rc;
	}

	private Table getTable(Connection conn, String name) throws SQLException {
		List<Field> fields = new DatabaseHelper(conn).getFields(name);
		PrimaryKey pkey = getPrimaryKey(conn, name);
		List<ForeignKey> fkeys = getForeignKeys(conn, name);
		List<Unique> uniques = getUniqueConstraints(conn, name);
		return new Table(name, fields, pkey, fkeys, uniques);
	}

	private List<Unique> getUniqueConstraints(Connection conn, String tableName) throws SQLException {
		List<Unique> rc = new DatabaseHelper(conn).getUniqueConstraints(tableName);
		return rc;
	}
}
