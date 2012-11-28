package at.r7r.schemaInject.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Data class representing table fields (aka colums)
 * @author Manuel Reithuber
 */
@XStreamAlias("field")
public class Field {
	@XStreamAsAttribute
	private String name;
	@XStreamAsAttribute
	private String type;
	@XStreamAsAttribute
	private String sequence;
	@XStreamAsAttribute
	private boolean nullable;
	
	@XStreamAlias("default")
	private String defaultValue;
	
	public String getName() {
		return name;
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
}
