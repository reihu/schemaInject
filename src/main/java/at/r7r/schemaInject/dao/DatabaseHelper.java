package at.r7r.schemaInject.dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import at.r7r.schemaInject.entity.Column;
import at.r7r.schemaInject.entity.Datatype;
import at.r7r.schemaInject.entity.ForeignKey;
import at.r7r.schemaInject.entity.Index;
import at.r7r.schemaInject.entity.PrimaryKey;
import at.r7r.schemaInject.entity.Table;
import at.r7r.schemaInject.entity.Unique;

/**
 * Class that does the lowlevel database stuff.
 * 
 * In the future there will be DBMS-specific subclasses that override the non-standard stuff if needed
 */
public class DatabaseHelper {
	private Connection conn;

	public DatabaseHelper (Connection connection) {
		conn = connection;
	}
	
	public void createTable(Table table) throws SQLException {
		String sql = new CreateBuilder().buildTable(table).join();
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.execute();
		}
		catch (SQLException e) {
			throw new SQLException("Error in SQL statement '"+sql+"'\n"+e.getMessage(), e);
		}
	}

	public List<Column> getColumns(String tableName) throws SQLException {
		ResultSet rs = conn.getMetaData().getColumns(null, null, tableName, "%");
		List<Column> rc = new ArrayList<Column>();

		while (rs.next()) {
			String name = rs.getString("COLUMN_NAME");
			String type = rs.getString("TYPE_NAME");
			Integer dimension = rs.getInt("COLUMN_SIZE");
			Integer fraction = rs.getInt("DECIMAL_DIGITS");
			String defaultValue = rs.getString("COLUMN_DEF");
			int nullable = rs.getInt("NULLABLE");
			
			if (dimension > 0) {
				type = type+'('+dimension+')';
			}
			type = type.toLowerCase();
			
			rc.add(new Column(null, name, new Datatype(type, dimension, fraction), nullable == DatabaseMetaData.columnNullable, defaultValue));
		}

		return rc;
	}

	public List<ForeignKey> getForeignKeys(String tableName) throws SQLException {
		ResultSet rs = conn.getMetaData().getImportedKeys(null, null, tableName);
		Map<String, ForeignKey> fkeyMap = new HashMap<String, ForeignKey>();

		while (rs.next()) {
			String fkeyName = rs.getString("FK_NAME");
			if (fkeyName == null) continue;
			String pkTable = rs.getString("PKTABLE_NAME");
			String pkColumn = rs.getString("PKCOLUMN_NAME");
			String fkColumn = rs.getString("FKCOLUMN_NAME");

			ForeignKey fkey;
			if (!fkeyMap.containsKey(fkeyName)) {
				fkey = new ForeignKey(null, fkeyName, pkTable);
				fkeyMap.put(fkeyName, fkey);
			}
			else fkey = fkeyMap.get(fkeyName);

			fkey.addColumn(fkColumn, pkColumn);
		}

		return new ArrayList<ForeignKey>(fkeyMap.values());
	}
	
	private List<Index> getIndices(String tableName, Boolean __unique) throws SQLException {
		ResultSet rs = conn.getMetaData().getIndexInfo(null, null, tableName, __unique != null ? __unique : false, true);
		Map<String,Index> indexMap = new HashMap<String, Index>();
		Map<String,List<String>> columnMap = new HashMap<String, List<String>>();
		Map<String,Boolean> uniqueMap = new HashMap<String, Boolean>();
		
		while (rs.next()) {
			String indexName = rs.getString("INDEX_NAME");
			String colName = rs.getString("COLUMN_NAME");
			boolean isUnique = !rs.getBoolean("NON_UNIQUE");

			if (__unique == null || __unique == isUnique) {
				if (!columnMap.containsKey(indexName)) columnMap.put(indexName, new ArrayList<String>());
				columnMap.get(indexName).add(colName);
				uniqueMap.put(indexName, isUnique);
			}
		}

		for (String indexName: uniqueMap.keySet()) {
			boolean isUnique = uniqueMap.get(indexName);
			Index idx;
			
			if (isUnique) {
				idx = new Unique(null, indexName, columnMap.get(indexName));
			}
			else {
				idx = new Index(null, indexName, columnMap.get(indexName));
			}
			
			indexMap.put(indexName, idx);
		}
		
		return new ArrayList<Index>(indexMap.values());
	}

	public List<Index> getIndices(String tableName) throws SQLException {
		return getIndices(tableName, false);
	}

	public List<Unique> getUniqueConstraints(String tableName) throws SQLException {
		List<Unique> rc = new ArrayList<Unique>();
		for (Index idx: getIndices(tableName, true)) {
			if (idx instanceof Unique) rc.add((Unique) idx);
		}
		
		return rc;
	}

	public PrimaryKey getPrimaryKey(String tableName) throws SQLException {
		ResultSet rs = conn.getMetaData().getPrimaryKeys(null, null, tableName);
		Map<Integer, String> columns = new TreeMap<Integer, String>();
		String pkeyName = null;

		while (rs.next()) {
			String columnName = rs.getString("COLUMN_NAME");
			int seq = rs.getInt("KEY_SEQ");
			if (pkeyName == null) pkeyName = rs.getString("PK_NAME");
			columns.put(seq, columnName);
		}

		return new PrimaryKey(null, pkeyName, new ArrayList<String>(columns.values()));
	}

	public List<String> listTables() throws SQLException {
		ResultSet rs = conn.getMetaData().getTables(null, null, "%", null);
		List<String> rc = new ArrayList<String>();

		while (rs.next()) {
//			String catalog = rs.getString("TABLE_CAT");
			String schema = rs.getString("TABLE_SCHEM");
			String name = rs.getString("TABLE_NAME");
			if (schema.toLowerCase().equals("public")) {
				rc.add(name);
			}
		}

		return rc;
	}
}
