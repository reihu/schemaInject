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
	private String fromColumn = null;

	@XStreamAsAttribute
	private String toTable = null;

	@XStreamAsAttribute
	private String toColumn = null;

	@XStreamImplicit(itemFieldName="from")
	private List<String> fromColumns = new LinkedList<String>();

	@XStreamImplicit(itemFieldName="to")
	private List<String> toColumns = new LinkedList<String>();

	public ForeignKey(Table parent, String name, List<String> from, String toTable, List<String> toColumns) {
		super(parent, name);
		this.fromColumn = setColumns(this.fromColumns, from);
		this.toTable = toTable;
		this.toColumn = setColumns(this.toColumns, toColumns);
	}
	
	public ForeignKey(Table parent, String name, String toTable) {
		super(parent, name);
		this.toTable = toTable;
	}
	
	public void addColumn(String fromColumn, String toColumn) {
		this.fromColumn = addToColumns(this.fromColumns, this.fromColumn, fromColumn);
		this.toColumn = addToColumns(this.toColumns, this.toColumn, toColumn);
	}

	@Override
	protected String autogenerateName(List<String> columns) {
		for (String column: getFrom()) {
			columns.add(column);
		}
		return "fkey";
	}
	
	public List<String> getFrom() {
		return getColumns(fromColumn, fromColumns);
	}

	public String getToTable() {
		return toTable;
	}

	public List<String> getTo() {
		return getColumns(toColumn, toColumns);
	}
}
