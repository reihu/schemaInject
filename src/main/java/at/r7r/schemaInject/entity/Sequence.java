package at.r7r.schemaInject.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("sequence")
public class Sequence {
	@XStreamAsAttribute
	private String name = null;
	
	@XStreamAsAttribute
	private long from = 0;
	
	@XStreamAsAttribute
	private long step = 0;
	
	long getFrom() {
		return from;
	}
	
	String getName() {
		return name;
	}
	
	long getStep() {
		return step;
	}
}
