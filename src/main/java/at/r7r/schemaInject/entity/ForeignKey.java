package at.r7r.schemaInject.entity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("fkey")
public class ForeignKey extends Constraint {
	@XStreamAsAttribute
	private String toTable = null;

	@XStreamImplicit(itemFieldName="from")
	private List<String> fromColumns = new LinkedList<String>();

	@XStreamImplicit(itemFieldName="to")
	private List<String> toColumns = new LinkedList<String>();

	public ForeignKey(Table parent, String name, List<String> from, String toTable, List<String> toColumns) {
		super(parent, name);
		this.fromColumns = from;
		this.toTable = toTable;
		this.toColumns = toColumns;
	}
	
	public ForeignKey(Table parent, String name, String toTable) {
		super(parent, name);
		this.toTable = toTable;
	}
	
	public void addColumn(String fromColumn, String toColumn) {
		if (this.fromColumns == null) this.fromColumns = new ArrayList<String>();
		if (this.toColumns == null) this.toColumns = new ArrayList<String>();

		this.fromColumns.add(fromColumn);
		this.toColumns.add(toColumn);
	}

	@Override
	protected String autogenerateName(List<String> columns) {
		for (String column: getFrom()) {
			columns.add(column);
		}
		return "fkey";
	}
	
	public List<String> getFrom() {
		if (fromColumns == null) fromColumns = new ArrayList<String>();
		return fromColumns;
	}

	public String getToTable() {
		return toTable;
	}

	public List<String> getTo() {
		if (toColumns == null) toColumns = new ArrayList<String>();
		return toColumns;
	}
}
