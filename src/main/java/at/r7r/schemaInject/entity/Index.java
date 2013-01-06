package at.r7r.schemaInject.entity;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class Index extends Constraint {
	@XStreamImplicit(itemFieldName="column")
	private List<String> columns = new ArrayList<String>();

	public Index(Table parent, String name, List<String> columns) {
		super(parent, name);
		this.columns = columns;
	}
	
	@Override
	protected String autogenerateName(List<String> columns) {
		for (String column: getColumns()) {
			columns.add(column);
		}
		return "idx";
	}

	public int getColumnCount() {
		return getColumns().size();
	}

	public List<String> getColumns() {
		if (columns == null) columns = new ArrayList<String>();
		return columns;
	}
}
