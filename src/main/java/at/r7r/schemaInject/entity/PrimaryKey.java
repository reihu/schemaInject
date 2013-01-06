package at.r7r.schemaInject.entity;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("pkey")
public class PrimaryKey extends Constraint {
	/**
	 * If there's only one column in the primary key, you can use the 'column' attribute to specify which one it is
	 */
	@XStreamAlias("column")
	@XStreamAsAttribute
	private String onlyColumn = null;

	/**
	 * To specify more than one column, use a <column> tag inside the <primary> tag for each column
	 */
	@XStreamImplicit(itemFieldName="column")
	private List<String> columns = new ArrayList<String>();

	public PrimaryKey(Table parent, String name, List<String> columns) {
		super(parent, name);

		this.onlyColumn = setColumns(this.columns, columns);
	}
	
	@Override
	protected String autogenerateName(List<String> columns) {
		// ignore the columns parameter as the table name is sufficient
		return "pkey";
	}

	public int getColumnCount() {
		return getColumnCount(onlyColumn, columns);
	}

	public List<String> getColumns() {
		return getColumns(onlyColumn, columns);
	}
}
