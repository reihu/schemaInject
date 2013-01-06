package at.r7r.schemaInject.entity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Datatype {
	// lowercase type name
	private String mTypeName;
	private Integer mDimension;
	private Integer mFraction;
	
	public Datatype(String type) {
		Pattern p = Pattern.compile("^([a-z]*)(\\(([0-9]*)(,([0-9]*))?\\))?");
		Matcher m = p.matcher(type.toLowerCase());

		mTypeName = m.group(1);
		String precision = m.group(3), fraction = m.group(4);
		mDimension = precision != null ? Integer.parseInt(precision) : null;
		mFraction = fraction != null ? Integer.parseInt(fraction) : null;
	}
	
	public Datatype(String typeName, Integer dimension, Integer fraction) {
		mTypeName = typeName;
		mDimension = dimension;
		mFraction = fraction;
	}
	
	@Override
	public String toString() {
		String rc = mTypeName;

		if (mDimension != null) {
			rc += "("+mDimension;
			if (mFraction != null) {
				rc += ","+mFraction;
			}
			rc += ")";
		}
		
		return rc;
	}
	
}
