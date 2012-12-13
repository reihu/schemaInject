package at.r7r.schemaInject.entity;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Data class representing XML table elements (and database tables)
 * @author Manuel Reithuber
 */
@XStreamAlias("table")
public class Table {
	/**
	 * Table name
	 */
	@XStreamAsAttribute
	private String name = "foo";
	
	/**
	 * List of fields in this table
	 */
	@XStreamImplicit(itemFieldName="field")
	private List<Field> fields = new ArrayList<Field>();

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

	public Table(String name, List<Field> fields, PrimaryKey pkey, List<ForeignKey> fkeys, List<Unique> uniques) {
		this.name = name;
		this.fields = fields;
		this.primaryKey = pkey;
		this.foreignKeys = fkeys;
		this.uniques = uniques;
	}

	/**
	 * Assigns names to each unnamed index stored in this table.
	 * 
	 * The index name schema is tableName_indexType_field1_..._fieldN
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
	 * Return the table name
	 * @return Table name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Return all the table's fields
	 * @return Fields (sometimes referred to as columns)
	 */
	public List<Field> getFields() {
		return fields;
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
}
