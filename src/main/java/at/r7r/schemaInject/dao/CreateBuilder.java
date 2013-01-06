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
	private SqlBuilder buildField(Column field) {
		SqlBuilder rc = new SqlBuilder(" ", false);
		rc.append("  \""+field.getName()+'"');
		rc.append(field.getType());
		if (!field.isNullable()) rc.append("NOT");
		rc.append("NULL");
		if (field.getSequence() != null) {
//			string.append("DEFAULT ");
// TODO implement sequence support			
		}
		else if (field.getDefault() != null) {
			rc.append("DEFAULT");
			rc.append('\''+field.getDefault()+'\'');
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
		
		SqlBuilder fields = new SqlBuilder(", ", false);
		for (String field: key.getFrom()) {
			fields.appendIdentifier(field);
		}
		sql.append('('+fields.join()+')');
		sql.append("REFERENCES");
		sql.appendIdentifier(key.getToTable());
		fields = new SqlBuilder(", ", false);
		for (String field: key.getTo()) {
			fields.appendIdentifier(field);
		}
		sql.append('('+fields.join()+')');
		
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
		SqlBuilder fields = new SqlBuilder(", ", false);
		for (String field: key.getFields()) {
			fields.appendIdentifier(field);
		}
		rc.append(fields);
		rc.append(")");
		return rc;
	}

	public SqlBuilder buildTable(Table table) {
		SqlBuilder rc = new SqlBuilder(" ", false);
		rc.append("CREATE TABLE");
		rc.appendIdentifier(table.getName());
		rc.append("(\n");
		
		SqlBuilder parts = new SqlBuilder(",", true);
		for (Column field: table.getFields()) {
			parts.append(buildField(field));
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
		SqlBuilder fields = new SqlBuilder(", ", false);
		for (String field: unique.getFields()) {
			fields.appendIdentifier(field);
		}
		
		rc.append(fields);
		rc.append(")");
		
		return rc;
	}

}
