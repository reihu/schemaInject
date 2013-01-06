package at.r7r.schemaInject.dao;

import at.r7r.schemaInject.entity.Column;
import at.r7r.schemaInject.entity.ForeignKey;
import at.r7r.schemaInject.entity.PrimaryKey;
import at.r7r.schemaInject.entity.Table;
import at.r7r.schemaInject.entity.Unique;

/**
 * Class that builds SQL 
 */
public class CreateBuilder {	
	private SqlBuilder buildColumn(Column column) {
		SqlBuilder rc = new SqlBuilder(" ", false);
		rc.append("  \""+column.getName()+'"');
		rc.append(column.getType());
		if (!column.isNullable()) rc.append("NOT");
		rc.append("NULL");
		if (column.getSequence() != null) {
//			string.append("DEFAULT ");
// TODO implement sequence support			
		}
		else if (column.getDefault() != null) {
			rc.append("DEFAULT");
			rc.append('\''+column.getDefault()+'\'');
		}
		return rc;
	}
	
	private SqlBuilder buildForeignKey(ForeignKey key) {
		SqlBuilder sql = new SqlBuilder(" ", false);
		
		sql.append(" ");
		if (key.hasName()) {
			sql.append("CONSTRAINT");
			sql.appendIdentifier(key.getName());
		}
		sql.append("FOREIGN KEY");
		
		SqlBuilder columns = new SqlBuilder(", ", false);
		for (String column: key.getFrom()) {
			columns.appendIdentifier(column);
		}
		sql.append('('+columns.join()+')');
		sql.append("REFERENCES");
		sql.appendIdentifier(key.getToTable());
		columns = new SqlBuilder(", ", false);
		for (String column: key.getTo()) {
			columns.appendIdentifier(column);
		}
		sql.append('('+columns.join()+')');
		
		return sql;
	}

	private SqlBuilder buildPrimaryKey(PrimaryKey key) {
		SqlBuilder rc = new SqlBuilder();
		rc.append("  ");
		if (key.hasName()) {
			rc.append("CONSTRAINT ");
			rc.appendIdentifier(key.getName());
			rc.append(" ");
		}
		rc.append("PRIMARY KEY (");
		SqlBuilder columns = new SqlBuilder(", ", false);
		for (String column: key.getColumns()) {
			columns.appendIdentifier(column);
		}
		rc.append(columns);
		rc.append(")");
		return rc;
	}

	public SqlBuilder buildTable(Table table) {
		SqlBuilder rc = new SqlBuilder(" ", false);
		rc.append("CREATE TABLE");
		rc.appendIdentifier(table.getName());
		rc.append("(\n");
		
		SqlBuilder parts = new SqlBuilder(",", true);
		for (Column column: table.getColumns()) {
			parts.append(buildColumn(column));
		}
		if (table.getPrimaryKey() != null) {
			parts.append(buildPrimaryKey(table.getPrimaryKey()));
		}
		for (ForeignKey fkey: table.getForeignKeys()) {
			parts.append(buildForeignKey(fkey));
		}
		
		for (Unique unique: table.getUniqueConstraints()) {
			parts.append(buildUnique(unique));
		}

		rc.append(parts.join());
		rc.append(");");
		
		return rc;
	}
	
	private SqlBuilder buildUnique(Unique unique) {
		SqlBuilder rc = new SqlBuilder(" ", false);
		
		rc.append(" ");
		if (unique.hasName()) {
			rc.append("CONSTRAINT");
			rc.appendIdentifier(unique.getName());
		}
		rc.append("UNIQUE");
		
		rc.append("(");
		SqlBuilder columns = new SqlBuilder(", ", false);
		for (String column: unique.getColumns()) {
			columns.appendIdentifier(column);
		}
		
		rc.append(columns);
		rc.append(")");
		
		return rc;
	}

}
