package at.r7r.schemaInject.xstreamConverter;

import at.r7r.schemaInject.entity.Datatype;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

public class DatatypeConverter extends AbstractSingleValueConverter {
	@Override
	public boolean canConvert(@SuppressWarnings("rawtypes") Class type) {
		return type.equals(Datatype.class);
	}

	@Override
	public Object fromString(String str) {
		return new Datatype(str);
	}

}
