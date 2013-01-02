package at.r7r.schemaInject.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("sequence")
public class Sequence extends Entity<Schema> {
	@XStreamAsAttribute
	private long from = 0;
	
	@XStreamAsAttribute
	private long step = 1;

	/**
	 * Sequence constructor
	 * @param parent Parent entity
	 * @param name Sequence name
	 * @param from Initial value
	 * @param step Sequence step
	 */
	public Sequence(Schema parent, String name, long from, long step) {
		super(parent, name);
		this.from = from;
		this.step = step;
	}
	
	/**
	 * Sequence constructor (with step=1)
	 * @param parent Parent entity
	 * @param name Sequence name
	 * @param from Initial value
	 */
	public Sequence(Schema parent, String name, long from) {
		this(parent, name, from, 1);
	}
	
	/**
	 * Sequence constructor (with from=0, step=1)
	 * @param parent Parent entity
	 * @param name Sequence name
	 */
	public Sequence(Schema parent, String name) {
		this(parent, name, 0, 1);
	}

	/**
	 * Returns the initial sequence value
	 * @return Initial value (default: 0)
	 */
	public long getFrom() {
		return from;
	}

	/**
	 * Returns the step value
	 * @return Step value (default: 1)
	 */
	public long getStep() {
		return step;
	}
}
