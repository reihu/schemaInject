package at.r7r.schemaInject.entity;

import java.util.LinkedList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("fkey")
public class ForeignKey extends Constraint {
	@XStreamAsAttribute
	private String name = null;

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

	public String getName() {
		return name;
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
