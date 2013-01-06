package at.r7r.schemaInject.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Data class representing table fields (aka colums)
 * @author Manuel Reithuber
 */
@XStreamAlias("column")
public class Column extends Entity<Table> {
	@XStreamAsAttribute
	private Datatype type;
	@XStreamAsAttribute
	private String sequence;
	@XStreamAsAttribute
	private boolean nullable;
	
	@XStreamAlias("default")
	private String defaultValue;
	
	public Column(Table parent, String name, Datatype type, boolean nullable, String defaultValue) {
		super(parent, name);
		this.type = type;
		this.nullable = nullable;
		this.defaultValue = defaultValue;
	}

	public Datatype getType() {
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
}
