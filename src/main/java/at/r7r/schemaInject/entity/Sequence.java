package at.r7r.schemaInject.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("sequence")
public class Sequence implements NamedEntity {
	@XStreamAsAttribute
	private String name = null;
	
	@XStreamAsAttribute
	private long from = 0;
	
	@XStreamAsAttribute
	private long step = 0;
	
	public long getFrom() {
		return from;
	}
	
	public String getName() {
		return name;
	}
	
	public long getStep() {
		return step;
	}
}
