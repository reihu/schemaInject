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
	@XStreamImplicit
	private List<Field> fields = new ArrayList<Field>();
	
	/**
	 * List of Unique constraints
	 */
	@XStreamImplicit
	private List<Unique> uniques = new ArrayList<Unique>();
	
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
	 * Return the table's unique constraints
	 * @return Unique Constraints
	 */
	public List<Unique> getUniqueConstraints() {
		return uniques;
	}
}
