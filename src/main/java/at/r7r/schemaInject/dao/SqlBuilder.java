package at.r7r.schemaInject.dao;

import java.util.LinkedList;
import java.util.List;

/**
 * Wrapper around {@link StringBuffer} that adds delimiter support and has some SQL-specific methods
 */
public class SqlBuilder {
	/**
	 * List of string parts (will be joined in {@link #join()})
	 */
	private List<String> mParts = new LinkedList<String>();
	/**
	 * Delimiter that will be inserted between the items of {@link #mParts}
	 */
	private String mDelimiter = null;
	
	/**
	 * Specifies if we should add a new line at the end of each item (including the last one)
	 */
	private boolean mNewLine = false;
	
	/**
	 * Constructs a {@link SqlBuilder} object and specifies delimiter and newLine
	 * @param delimiter Delimiter to insert between items
	 * @param newLine Specifies whether to add a newLine after each item (even the last one) or not
	 */
	public SqlBuilder(String delimiter, boolean newLine) {
		mDelimiter = delimiter;
		mNewLine = newLine;
	}
	
	/**
	 * Constructs a {@link SqlBuilder} without delimiter and newLine set to false
	 */
	public SqlBuilder() {
		
	}
	
	/**
	 * Appends a String to the buffer
	 * @param sql String to add
	 */
	public void append(String sql) {
		mParts.add(sql);
	}
	
	/**
	 * Appends another SqlBuilder object (short form of {@code append(sql.join())})
	 * @param sql SqlBuilder object to append
	 */
	public void append(SqlBuilder sql) {
		append(sql.join());
	}
	
	/**
	 * Appends an SQL identifier (e.g. a column or table name) enclosed in double brackets
	 * @param name Identifier name
	 */
	public void appendIdentifier(String name) {
		// TODO escape identifier name 
		append('\"'+name+'\"');
	}
	
	/**
	 * Joins the parts using the specified delimiter (if present) between and a newline character (if specified) after each item 
	 * @return Joined String
	 */
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
	
	public void prepend(String str) {
		mParts.add(0, str);
	}
	
	/**
	 * Simply calls {@link #join()}
	 * @return Joined {@link String}
	 */
	public String toString() {
		return join();
	}
}
