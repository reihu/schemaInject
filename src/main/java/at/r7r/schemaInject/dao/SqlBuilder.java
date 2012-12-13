package at.r7r.schemaInject.dao;

import java.util.LinkedList;
import java.util.List;

public class SqlBuilder {
	private List<String> mParts = new LinkedList<String>();
	private String mDelimiter = null;
	private boolean mNewLine = false;
	
	public SqlBuilder(String delimiter, boolean newLine) {
		mDelimiter = delimiter;
		mNewLine = newLine;
	}
	
	public SqlBuilder() {
		
	}
	
	public void append(String sql) {
		mParts.add(sql);
	}
	
	public void append(SqlBuilder sql) {
		mParts.add(sql.join());
	}
	
	public void appendIdentifier(String name) {
		mParts.add('\"'+name+'\"');
	}
	
	public String join() {
		StringBuffer buffer = new StringBuffer();
		int i = 0, size = mParts.size();
		
		for (String part: mParts) {
			buffer.append(part);
			if (mDelimiter != null && ++i < size) buffer.append(mDelimiter);
			if (mNewLine) buffer.append('\n');
			
		}
		return buffer.toString();
	}
}
