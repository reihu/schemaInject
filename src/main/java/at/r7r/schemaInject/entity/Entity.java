package at.r7r.schemaInject.entity;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public abstract class Entity {
	/**
	 * The name of the constraint (set it to null to use implicit names)
	 */
	@XStreamAsAttribute
	private String name;
	
	/**
	 * Default constructor
	 * @param name Entity name
	 */
	public Entity(String name) {
		this.name = name;
	}
	
	/**
	 * Name getter
	 * @return Entity name
	 */
	public final String getName() {
		return name;
	}

	/**
	 * Returns whether this entity has a non-empty name
	 * @return name != null && !name.isEmpty() 
	 */
	public boolean hasName() {
		return name != null && !name.isEmpty();
	}

	/**
	 * Sets the name of the Entity (currently only used by {@link Constraint.assignNameIfUnnamed()}
	 * @param name New Entity name
	 */
	protected void setName(String name) {
		this.name = name;
	}
}
