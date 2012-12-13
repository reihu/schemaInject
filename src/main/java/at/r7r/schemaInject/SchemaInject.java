package at.r7r.schemaInject;

import java.sql.Connection;
import java.sql.SQLException;

import at.r7r.schemaInject.dao.DatabaseHelper;
import at.r7r.schemaInject.entity.Field;
import at.r7r.schemaInject.entity.Schema;
import at.r7r.schemaInject.entity.Table;

public class SchemaInject {
	private void checkField(DatabaseHelper db, Field field) {
		
	}
	
	private void checkTable(DatabaseHelper db, Table table) throws SQLException {
		if (!db.listTables().contains(table.getName())) {
			db.createTable(table);
		}
		for (Field field: table.getFields()) {
			checkField(db, field);
		}
		System.out.println("Checking table "+table.getName());
	}

	public void checkSchema(Connection conn, Schema schema) throws SQLException {
		DatabaseHelper db = new DatabaseHelper(conn); 
		for (Table table: schema.getTables()) {
			checkTable(db, table);
		}
	}
}
