package at.r7r.schemaInject.entity;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("unique")
public class Unique extends Index {	
	public Unique(Table parent, String name, List<String> fields) {
		super(parent, name, fields);
	}
	
	@Override
	protected String autogenerateName(List<String> fields) {
		super.autogenerateName(fields);
		return "uniq";
	}
}
