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
public class Schema extends Entity<Schema> {
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
	 * Default constructor
	 * 
	 * (Calls Entity(""), Schemas can't have names or parents)
	 */
	public Schema() {
		super(null, "");
	}

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
	 * Returns all sequences defined in this schema
	 * @return Sequence list (might be empty)
	 */
	public List<Sequence> getSequences() {
		return sequences;
	}
	
	public Sequence getSequence(String name) {
		return getItemByName(getSequences(), name);
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
		return getItemByName(getTables(), name);
	}
	
	/**
	 * Add a table to the schema
	 * @param t Table to add
	 * TODO check whether we need this outside of test use cases
	 */
	public void addTable(Table t) {
		tables.add(t);
	}
	
	public void setRevision(int rev) {
		this.revision = rev;
	}
	
	/// TODO implement another equals()-like method that supports callbacks (for automatic schema updates)
	@Override
	public boolean equals(Object otherObject) {
		if (otherObject == null) return false;
		if (!(otherObject instanceof Schema)) return false;
		Schema other = (Schema) otherObject;

		if (!getMetaTable().equals(other.getMetaTable())) return false;
		if (other.getRevision() != getRevision()) return false;

		if (getTables().size() != other.getTables().size()) return false;
		for (Table table: getTables()) {
			Table otherTable = other.getTable(table.getName());
			if (otherTable == null) return false;
			if (!table.equals(otherTable)) return false;
		}

		if (getSequences().size() != other.getSequences().size()) return false;
		for (Sequence seq: getSequences()) {
			Sequence otherSequence = other.getSequence(seq.getName());
			if (otherSequence == null) return false;
			if (!seq.equals(otherSequence)) return false;
		}
		
		return true;
	}
	
	/// TODO move these two methods into another class
	static <T extends Entity<?>> T getItemByName(List<T> list, String name) {
		if (name == null) return null;
		
		for (T e: list) {
			if (name.equals(e.getName())) return e;
		}
		return null;
	}
	
	static boolean listsEqual(List<? extends Entity<?>> a, List<? extends Entity<?>> b) {
		if (a == null && b == null) return true;
		else if (a == null) return false;
		else if (b == null) return false;
		
		if (a.size() != b.size()) return false;
		for (Entity<?> entity: a) {
			Entity<?> otherEntity = getItemByName(b, entity.getName());
			if (otherEntity == null) return false;
			if (!entity.equals(otherEntity)) return false;
		}

		return true;
	}
}
