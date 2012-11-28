package at.r7r.schemaInject.entity;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("unique")
public class Unique extends Constraint {
	@XStreamImplicit(itemFieldName="field")
	private List<String> fields = null;

	@XStreamAlias("field")
	@XStreamAsAttribute
	private String oneField = null;
	
	public int getFieldCount() {
		int rc = 0;
		if (oneField != null) rc = 1;
		if (fields != null) rc = fields.size();
		return rc;
	}
	
	public List<String> getFields() {
		List<String> rc;
		if (fields != null) rc = fields;
		else {
			rc = new ArrayList<String>();
			rc.add(oneField);
		}
		return rc;
	}
}
