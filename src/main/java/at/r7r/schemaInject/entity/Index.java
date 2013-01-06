package at.r7r.schemaInject.entity;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class Index extends Constraint {
	@XStreamImplicit(itemFieldName="column")
	private List<String> columns = new ArrayList<String>();

	@XStreamAlias("column")
	@XStreamAsAttribute
	private String oneColumn = null;

	public Index(Table parent, String name, List<String> columns) {
		super(parent, name);
		oneColumn = setColumns(this.columns, columns);
	}
	
	@Override
	protected String autogenerateName(List<String> columns) {
		for (String column: getColumns()) {
			columns.add(column);
		}
		return "idx";
	}

	public int getColumnCount() {
		return getColumnCount(oneColumn, columns);
	}

	public List<String> getColumns() {
		return getColumns(oneColumn, columns);
	}
}
