package at.r7r.schemaInject.entity;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("pkey")
public class PrimaryKey extends Constraint {
	/**
	 * The name of the private key (set it to null to use implicit keys)
	 */
	@XStreamAsAttribute
	private String name = null;

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
		this.name = name;

		if (fields.size() == 1) {
			this.onlyField = fields.get(0);
		}
		else this.fields = fields;
	}

	public int getFieldCount() {
		return getFieldCount(onlyField, fields);
	}

	public List<String> getFields() {
		return getFields(onlyField, fields);
	}

	public String getName() {
		return name;
	}
}
