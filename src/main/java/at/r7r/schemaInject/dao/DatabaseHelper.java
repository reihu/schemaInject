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

import at.r7r.schemaInject.entity.Field;
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

	public boolean compareTypes(String firstType, String secondType) {
		return firstType.equalsIgnoreCase(secondType);
	}
	
	public void createTable(Table table) throws SQLException {
		String sql = new CreateBuilder().buildTable(table).join();
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.execute();
	}

	public List<Field> getFields(String tableName) throws SQLException {
		ResultSet rs = conn.getMetaData().getColumns(null, null, tableName, "%");
		List<Field> rc = new ArrayList<Field>();

		while (rs.next()) {
			String name = rs.getString(4);
			String type = rs.getString(6);
			String defaultValue = rs.getString(13);
			int nullable = rs.getInt(11);
			rc.add(new Field(null, name, type, nullable == DatabaseMetaData.columnNullable, defaultValue));
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
			String pkField = rs.getString("PKCOLUMN_NAME");
			String fkField = rs.getString("FKCOLUMN_NAME");

			ForeignKey fkey;
			if (!fkeyMap.containsKey(fkeyName)) {
				fkey = new ForeignKey(null, fkeyName, pkTable);
				fkeyMap.put(fkeyName, fkey);
			}
			else fkey = fkeyMap.get(fkeyName);

			fkey.addField(fkField, pkField);
		}

		return new ArrayList<ForeignKey>(fkeyMap.values());
	}
	
	private List<Index> getIndices(String tableName, Boolean __unique) throws SQLException {
		ResultSet rs = conn.getMetaData().getIndexInfo(null, null, tableName, __unique != null ? __unique : false, true);
		Map<String,Index> indexMap = new HashMap<String, Index>();
		Map<String,List<String>> fieldMap = new HashMap<String, List<String>>();
		Map<String,Boolean> uniqueMap = new HashMap<String, Boolean>();
		
		while (rs.next()) {
			String indexName = rs.getString("INDEX_NAME");
			String colName = rs.getString("COLUMN_NAME");
			boolean isUnique = !rs.getBoolean("NON_UNIQUE");

			if (__unique == null || __unique == isUnique) {
				if (!fieldMap.containsKey(indexName)) fieldMap.put(indexName, new ArrayList<String>());
				fieldMap.get(indexName).add(colName);
				uniqueMap.put(indexName, isUnique);
			}
		}

		for (String indexName: uniqueMap.keySet()) {
			boolean isUnique = uniqueMap.get(indexName);
			Index idx;
			
			if (isUnique) {
				idx = new Unique(null, indexName, fieldMap.get(indexName));
			}
			else {
				idx = new Index(null, indexName, fieldMap.get(indexName));
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
		Map<Integer, String> fields = new TreeMap<Integer, String>();
		String pkeyName = null;

		while (rs.next()) {
			String fieldName = rs.getString("COLUMN_NAME");
			int seq = rs.getInt("KEY_SEQ");
			if (pkeyName == null) pkeyName = rs.getString("PK_NAME");
			fields.put(seq, fieldName);
		}

		return new PrimaryKey(null, pkeyName, new ArrayList<String>(fields.values()));
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
