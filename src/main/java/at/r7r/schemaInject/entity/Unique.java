package at.r7r.schemaInject.entity;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("unique")
public class Unique extends Index {	
	public Unique(Table parent, String name, List<String> columns) {
		super(parent, name, columns);
	}
	
	@Override
	protected String autogenerateName(List<String> columns) {
		super.autogenerateName(columns);
		return "uniq";
	}
}
