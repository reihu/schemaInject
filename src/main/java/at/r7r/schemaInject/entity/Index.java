package at.r7r.schemaInject.entity;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class Index extends Constraint {
	@XStreamImplicit(itemFieldName="field")
	private List<String> fields = new ArrayList<String>();

	@XStreamAlias("field")
	@XStreamAsAttribute
	private String oneField = null;

	public Index(String name, List<String> fields) {
		super(name);
		oneField = setFields(this.fields, fields);
	}
	
	@Override
	protected String autogenerateName(List<String> fields) {
		for (String field: getFields()) {
			fields.add(field);
		}
		return "idx";
	}

	public int getFieldCount() {
		return getFieldCount(oneField, fields);
	}

	public List<String> getFields() {
		return getFields(oneField, fields);
	}
}
