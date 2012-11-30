package at.r7r.schemaInject.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for all constraints (PrimaryKey, ForeignKey, Unique, Check)
 * @author Manuel Reithuber
 */
public class Constraint {
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
}
