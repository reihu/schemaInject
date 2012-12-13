package at.r7r.schemaInject.entity;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Schema class (represents the root of the XML documents)
 * @author Manuel Reithuber
 */
@XStreamAlias("schema")
public class Schema {
	/**
	 * The current revision of the database schema.
	 * Everytime you change something in the .xml file you should increment this value
	 */
	@XStreamAsAttribute
	private int revision = 0;

	/**
	 * We use a meta table to store the current schema revision.
	 * You can specify this table here (however keep in mind that it's not a good
	 * idea to change that afterwards)
	 */
	@XStreamAsAttribute
	private String metaTable = "_schemaInject";

	/**
	 * Table prefix. If you set this to a non-empty string, every table will be
	 * created using this value as prefix (e.g. "foobar_")
	 */
	@XStreamAsAttribute
	private String prefix = null;

	/**
	 * Contains all the explicitly defined sequences
	 */
	@XStreamImplicit(itemFieldName="sequence")
	private List<Sequence> sequences = new ArrayList<Sequence>();

	/**
	 * Contains all the managed tables
	 */
	@XStreamImplicit
	private List<Table> tables = new ArrayList<Table>();
	
	/**
	 * Assigns names to all the indices that don't have assigned names in the schema.xml
	 * 
	 * The names will look like tableName_idxType_field1_..._fieldN.
	 * That makes sure indices will be recognized in later runs.
	 * 
	 * This method calls Table.assignNamesToUnnamedIndices() for each child table.
	 * It is called by Util.readSchema()
	 */
	public void assignNamesToUnnamedIndices() {
		for (Table table: tables) {
			table.assignNamesToUnnamedIndices();
		}
		
/*		for (Sequence seq: sequences) {
			//seq.assignNameIfUnnamed(); <-- Sequences have to have names
		}*/
	}
	
	/**
	 * Returns the current revision of the XML schema
	 * @return revision (default: 0)
	 */
	public int getRevision() {
		return revision;
	}
	
	/**
	 * Returns the name of the meta table which is used to save the DBs revision
	 * @return meta table name (default: "_schemaInject");
	 */
	public String getMetaTable() {
		return metaTable;
	}
	
	/**
	 * Returns the prefix for all the tables that will be managed
	 * @return Prefix (default: null)
	 */
	public String getTablePrefix() {
		return prefix;
	}

	/**
	 * Returns all sequences defined in this schema
	 * @return Sequence list (might be empty)
	 */
	public List<Sequence> getSequences() {
		return sequences;
	}

	/**
	 * Returns all tables defined in this schema
	 * @return Table list (might be empty)
	 */
	public List<Table> getTables() {
		return tables;
	}
	
	/**
	 * Returns a table with a specific name
	 * @param name Table name
	 * @return Table or null if not found
	 */
	public Table getTable(String name) {
		for (Table table: getTables()) {
			if (table.getName().equals(name)) {
				return table;
			}
		}
		return null;
	}
	
	/**
	 * Add a table to the schema
	 * @param t Table to add
	 * TODO check whether we need this outside of test use cases
	 */
	public void addTable(Table t) {
		tables.add(t);
	}
}
