package at.r7r.schemaInject.entity;

import at.r7r.schemaInject.dao.DatabaseHelper;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Data class representing table fields (aka colums)
 * @author Manuel Reithuber
 */
@XStreamAlias("field")
public class Column extends Entity<Table> {
	@XStreamAsAttribute
	private String type;
	@XStreamAsAttribute
	private String sequence;
	@XStreamAsAttribute
	private boolean nullable;
	
	@XStreamAlias("default")
	private String defaultValue;
	
	public Column(Table parent, String name, String type, boolean nullable, String defaultValue) {
		super(parent, name);
		this.type = type;
		this.nullable = nullable;
		this.defaultValue = defaultValue;
	}

	public String getType() {
		return type;
	}
	
	public String getSequence() {
		return sequence;
	}
	
	public boolean isNullable() {
		return nullable;
	}
	
	public String getDefault() {
		return defaultValue;
	}
	
	@Override
	protected boolean valueEquals(java.lang.reflect.Field field, Object thisValue, Object otherValue,
			at.r7r.schemaInject.entity.Entity.EqualityHandler handler) {
		if (field.getName().equals("type")) {
			return new DatabaseHelper(null).compareTypes((String) thisValue, (String) otherValue);
		}
		else return super.valueEquals(field, thisValue, otherValue, handler);
	}
}
