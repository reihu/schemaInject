package at.r7r.schemaInject.entity;

import java.util.LinkedList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("fkey")
public class ForeignKey extends Constraint {
	@XStreamAsAttribute
	@XStreamAlias("from")
	private String fromField = null;

	@XStreamAsAttribute
	private String toTable = null;

	@XStreamAsAttribute
	private String toField = null;

	@XStreamImplicit(itemFieldName="from")
	private List<String> fromFields = new LinkedList<String>();

	@XStreamImplicit(itemFieldName="to")
	private List<String> toFields = new LinkedList<String>();

	public ForeignKey(String name, List<String> from, String toTable, List<String> toFields) {
		super(name);
		this.fromField = setFields(this.fromFields, from);
		this.toTable = toTable;
		this.toField = setFields(this.toFields, toFields);
	}
	
	public ForeignKey(String name, String toTable) {
		super(name);
		this.toTable = toTable;
	}
	
	public void addField(String fromField, String toField) {
		this.fromField = addToFields(this.fromFields, this.fromField, fromField);
		this.toField = addToFields(this.toFields, this.toField, toField);
	}

	@Override
	protected String autogenerateName(List<String> fields) {
		for (String field: getFrom()) {
			fields.add(field);
		}
		return "fkey";
	}
	
	public List<String> getFrom() {
		return getFields(fromField, fromFields);
	}

	public String getToTable() {
		return toTable;
	}

	public List<String> getTo() {
		return getFields(toField, toFields);
	}
}
