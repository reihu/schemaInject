package at.r7r.schemaInject.entity;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("unique")
public class Unique extends Constraint {
	@XStreamImplicit(itemFieldName="field")
	private List<String> fields = null;

	@XStreamAlias("field")
	@XStreamAsAttribute
	private String oneField = null;
	
	public int getFieldCount() {
		return getFieldCount(oneField, fields);
	}
	
	public List<String> getFields() {
		return getFields(oneField, fields);
	}
}
