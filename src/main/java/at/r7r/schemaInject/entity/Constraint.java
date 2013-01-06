package at.r7r.schemaInject.entity;

import java.util.ArrayList;
import java.util.List;

import at.r7r.schemaInject.dao.SqlBuilder;

/**
 * Base class for all constraints (PrimaryKey, ForeignKey, Unique, Check)
 */
public abstract class Constraint extends Entity<Table> {
	public Constraint(Table parent, String name) {
		super(parent, name);
	}
	
	protected abstract String autogenerateName(List<String> columns);
	
	public void assignNameIfUnnamed(String tableName) {
		List<String> columns = new ArrayList<String>();
		String indexType = autogenerateName(columns);
		
		if (getName() == null || getName().length() == 0) {
			SqlBuilder name = new SqlBuilder("_", false);
			name.append(tableName);
			name.append(indexType);
			for (String column: columns) {
				name.append(column);
			}
			setName(name.join());
		}		

	}
	
	protected static String addToColumns(List<String> tgtList, String tgtString, String columnToAdd) {
		int oldCount = getColumnCount(tgtString, tgtList); 
		if (oldCount == 0) {
			tgtList.clear();
			return columnToAdd;
		}
		else if (oldCount == 1 && tgtString != null) {
			tgtList.clear();
			tgtList.add(tgtString);
			tgtList.add(columnToAdd);
			return null;
		}
		else {
			tgtList.add(columnToAdd);
			return null;
		}
	}
	
	/**
	 * Helper method to get the column count for values which can be described using either an attribute or a tag list
	 * @param attr XML attribute value
	 * @param tagList XML tag list
	 * @return Column count (if attr != null, tagList will be ignored and 1 will be returned)
	 */
	protected static int getColumnCount(String attr, List<String> tagList) {
		int rc = 0;
		if (attr != null) rc = 1;
		else if (tagList != null) rc = tagList.size();
		return rc;
	}

	/**
	 * Helper method to get the column for values which can be described using either an attribute or a tag list
	 * @param attr XML attribute value
	 * @param tagList XML tag list
	 * @return Columns (if attr != null, it returns a new list with just that one value)
	 */
	protected static List<String> getColumns(String attr, List<String> tagList) {
		List<String> rc;
		if (attr != null) {
			rc = new ArrayList<String>();
			rc.add(attr);
		}
		else {
			rc = tagList;
			if (rc == null) rc = new ArrayList<String>();
		}

		return rc;
	}
	
	/**
	 * Checks the size of srcList and either returns the only element or copies the list to targetList
	 * @param tgtList (output parameter) srcList.size() != 1 ? srcList : empty list 
	 * @param srcList Source list
	 * @return the only column or null (if srcList.size() != 1)
	 */
	protected static String setColumns(List<String> tgtList, List<String> srcList) {
		tgtList.clear();
		if (srcList.size() == 1) {
			return srcList.get(0);
		}
		else {
			tgtList.addAll(srcList);
			return null;
		}
	}
}
