package at.r7r.schemaInject.entity;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("pkey")
public class PrimaryKey extends Constraint {
	@XStreamImplicit(itemFieldName="column")
	private List<String> columns = new ArrayList<String>();

	public PrimaryKey(Table parent, String name, List<String> columns) {
		super(parent, name);

		this.columns = columns;
	}
	
	@Override
	protected String autogenerateName(List<String> columns) {
		// ignore the columns parameter as the table name is sufficient
		return "pkey";
	}

	public int getColumnCount() {
		return getColumns().size();
	}

	public List<String> getColumns() {
		if (columns == null) columns = new ArrayList<String>();
		return columns;
	}
}
