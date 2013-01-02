package at.r7r.schemaInject.entity;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

public abstract class Entity<ParentType extends Entity<?>> {
	/**
	 * The name of the constraint (set it to null to use implicit names)
	 */
	@XStreamAsAttribute
	private String name;
	
	/**
	 * Link to the parent entity (may be null)
	 */
	@XStreamOmitField
	private ParentType parent;
	
	/**
	 * Default constructor
	 * @param name Entity name
	 */
	public Entity(ParentType parent, String name) {
		this.parent = parent;
		this.name = name;
	}
	
	/**
	 * Name getter
	 * @return Entity name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the entity's Parent
	 * @return Parent entity (might be null)
	 */
	public ParentType getParent() {
		return parent;
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
