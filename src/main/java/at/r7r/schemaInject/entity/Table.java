package at.r7r.schemaInject.entity;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Data class representing XML table elements (and database tables)
 * @author Manuel Reithuber
 */
@XStreamAlias("table")
public class Table extends Entity<Schema> {
	/**
	 * List of columns in this table
	 */
	@XStreamImplicit(itemFieldName="column")
	private List<Column> columns = new ArrayList<Column>();

	/**
	 * Primary key
	 */
	@XStreamAlias("pkey")
	private PrimaryKey primaryKey = null;

	/**
	 * Foreign key constraints
	 */
	@XStreamImplicit(itemFieldName="fkey")
	private List<ForeignKey> foreignKeys = new ArrayList<ForeignKey>();
	
	/**
	 * List of Unique constraints
	 */
	@XStreamImplicit(itemFieldName="unique")
	private List<Unique> uniques = new ArrayList<Unique>();

	public Table(Schema parent, String name, List<Column> columns, PrimaryKey pkey, List<ForeignKey> fkeys, List<Unique> uniques) {
		super(parent, name);
		this.columns = columns;
		this.primaryKey = pkey;
		this.foreignKeys = fkeys;
		this.uniques = uniques;
	}

	/**
	 * Assigns names to each unnamed index stored in this table.
	 * 
	 * The index name schema is tableName_indexType_col1_..._colN
	 * 
	 * Doing that will make sure we'll correctly recognize keys on update 
	 * 
	 * This method is automatically called by Schema.assignNamesToUnnamedIndices() (which in turn is called by Util.readSchema())
	 */
	public void assignNamesToUnnamedIndices() {
		primaryKey.assignNameIfUnnamed(getName());
		for (ForeignKey fkey: getForeignKeys()) {
			fkey.assignNameIfUnnamed(getName());
		}
		
		for (Unique unique: getUniqueConstraints()) {
			unique.assignNameIfUnnamed(getName());
		}
	}

	/**
	 * Return all the table's columns
	 * @return Columns (sometimes referred to as columns)
	 */
	public List<Column> getColumns() {
		return columns;
	}
	
	/**
	 * Returns the primary key definition (if any)
	 * @return PrimaryKey or null
	 */
	public PrimaryKey getPrimaryKey() {
		return primaryKey;
	}
	
	/**
	 * Returns a list of foreign keys defined in this table
	 * @return Foreign key constraints
	 */
	public List<ForeignKey> getForeignKeys() {
		if (foreignKeys == null) foreignKeys = new ArrayList<ForeignKey>();
		return foreignKeys;
	}
	
	/**
	 * Return the table's unique constraints
	 * @return Unique Constraints
	 */
	public List<Unique> getUniqueConstraints() {
		if (uniques == null) uniques = new ArrayList<Unique>();
		return uniques;
	}
	
	public void polish() {
		for (Column column: getColumns()) {
			column.setParent(this);
			column.polish();
		}
		
		for (ForeignKey fkey: getForeignKeys()) {
			fkey.setParent(this);
			fkey.polish();
		}
		
		PrimaryKey pkey = getPrimaryKey();
		if (pkey != null) {
			pkey.setParent(this);
			pkey.polish();
		}
		
		for (Unique uniq: getUniqueConstraints()) {
			uniq.setParent(this);
			uniq.polish();
		}
	}
}
