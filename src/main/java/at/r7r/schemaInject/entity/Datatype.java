package at.r7r.schemaInject.entity;

public class Datatype {
	// lowercase type name
	private String mTypeName = null;
	private Integer mDimension = null;
	private Integer mFraction = null;
	
	public Datatype(String type) {
		type = type.trim().toLowerCase();
		int bracketIndex = type.indexOf('(');
		if (bracketIndex > -1 && type.endsWith(")")) {
			mTypeName = type.substring(0, bracketIndex);
			type = type.substring(bracketIndex+1, type.length()-1);
			int commaPos = type.indexOf(',');
			if (commaPos > -1) {
				mDimension = Integer.parseInt(type.substring(0, commaPos));
				mFraction = Integer.parseInt(type.substring(commaPos+1));
			}
			else mDimension = Integer.parseInt(type);
		}
		else {
			mTypeName = type;
		}
	}
	
	public Datatype(String typeName, Integer dimension, Integer fraction) {
		mTypeName = typeName.toLowerCase();
		mDimension = dimension > 0 ? dimension : null;
		mFraction = fraction > 0 ? fraction : null;
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
