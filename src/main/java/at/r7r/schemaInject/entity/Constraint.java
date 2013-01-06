package at.r7r.schemaInject.entity;

import java.util.ArrayList;
import java.util.List;

import at.r7r.schemaInject.dao.SqlBuilder;

/**
 * Base class for all constraints (PrimaryKey, ForeignKey, Unique, Check)
 */
public abstract class Constraint extends Entity<Table> {
	public Constraint(Table parent, String name) {
		super(parent, name);
	}
	
	protected abstract String autogenerateName(List<String> columns);
	
	public void assignNameIfUnnamed(String tableName) {
		List<String> columns = new ArrayList<String>();
		String indexType = autogenerateName(columns);
		
		if (getName() == null || getName().length() == 0) {
			SqlBuilder name = new SqlBuilder("_", false);
			name.append(tableName);
			name.append(indexType);
			for (String column: columns) {
				name.append(column);
			}
			setName(name.join());
		}		

	}
}
