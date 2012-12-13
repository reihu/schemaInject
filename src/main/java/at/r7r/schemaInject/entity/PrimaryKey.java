package at.r7r.schemaInject.entity;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("pkey")
public class PrimaryKey extends Constraint {
	/**
	 * If there's only one column in the primary key, you can use the 'field' attribute to specify which one it is
	 */
	@XStreamAlias("field")
	@XStreamAsAttribute
	private String onlyField = null;

	/**
	 * To specify more than one column, use a <field> tag inside the <primary> tag for each column
	 */
	@XStreamImplicit(itemFieldName="field")
	private List<String> fields = new ArrayList<String>();

	public PrimaryKey(String name, List<String> fields) {
		super(name);

		this.onlyField = setFields(this.fields, fields);
	}
	
	@Override
	protected String autogenerateName(List<String> fields) {
		// ignore the fields parameter as the table name is sufficient
		return "pkey";
	}

	public int getFieldCount() {
		return getFieldCount(onlyField, fields);
	}

	public List<String> getFields() {
		return getFields(onlyField, fields);
	}
}
