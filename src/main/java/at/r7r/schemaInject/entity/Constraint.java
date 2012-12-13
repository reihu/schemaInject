package at.r7r.schemaInject.entity;

import java.util.ArrayList;
import java.util.List;

import at.r7r.schemaInject.dao.SqlBuilder;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Base class for all constraints (PrimaryKey, ForeignKey, Unique, Check)
 * @author Manuel Reithuber
 */
public abstract class Constraint implements NamedEntity {
	/**
	 * The name of the constraint (set it to null to use implicit names)
	 */
	@XStreamAsAttribute
	private String name = null;

	public Constraint(String name) {
		this.name = name;
	}
	
	protected abstract String autogenerateName(List<String> fields);
	
	public void assignNameIfUnnamed(String tableName) {
		List<String> fields = new ArrayList<String>();
		String indexType = autogenerateName(fields);
		
		if (getName() == null || getName().length() == 0) {
			SqlBuilder name = new SqlBuilder("_", false);
			name.append(tableName);
			name.append(indexType);
			for (String field: fields) {
				name.append(field);
			}
			setName(name.join());
		}		

	}
	
	protected static String addToFields(List<String> tgtList, String tgtString, String fieldToAdd) {
		int oldCount = getFieldCount(tgtString, tgtList); 
		if (oldCount == 0) {
			tgtList.clear();
			return fieldToAdd;
		}
		else if (oldCount == 1 && tgtString != null) {
			tgtList.clear();
			tgtList.add(tgtString);
			tgtList.add(fieldToAdd);
			return null;
		}
		else {
			tgtList.add(fieldToAdd);
			return null;
		}
	}
	
	/**
	 * Helper method to get the field count for values which can be described using either an attribute or a tag list
	 * @param attr XML attribute value
	 * @param tagList XML tag list
	 * @return Field count (if attr != null, tagList will be ignored and 1 will be returned)
	 */
	protected static int getFieldCount(String attr, List<String> tagList) {
		int rc = 0;
		if (attr != null) rc = 1;
		else if (tagList != null) rc = tagList.size();
		return rc;
	}

	/**
	 * Helper method to get the fields for values which can be described using either an attribute or a tag list
	 * @param attr XML attribute value
	 * @param tagList XML tag list
	 * @return Fields (if attr != null, it returns a new list with just that one value)
	 */
	protected static List<String> getFields(String attr, List<String> tagList) {
		List<String> rc;
		if (attr != null) {
			rc = new ArrayList<String>();
			rc.add(attr);
		}
		else rc = tagList;

		return rc;
	}

	public String getName() {
		return name;
	}

	public boolean hasName() {
		return name != null && name.length() > 0;
	}
	
	/**
	 * Checks the size of srcList and either returns the only element or copies the list to targetList
	 * @param tgtList (output parameter) srcList.size() != 1 ? srcList : empty list 
	 * @param srcList Source list
	 * @return the only field or null (if srcList.size() != 1)
	 */
	protected static String setFields(List<String> tgtList, List<String> srcList) {
		tgtList.clear();
		if (srcList.size() == 1) {
			return srcList.get(0);
		}
		else {
			tgtList.addAll(srcList);
			return null;
		}
	}
	
	protected void setName(String name) {
		this.name = name;
	}
}
