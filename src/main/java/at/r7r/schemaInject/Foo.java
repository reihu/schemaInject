package at.r7r.schemaInject;

import java.io.File;

import com.thoughtworks.xstream.XStream;

import at.r7r.schemaInject.entity.Schema;
import at.r7r.schemaInject.entity.Sequence;
import at.r7r.schemaInject.entity.Table;

public class Foo {
	/**
	 * @param args ignored
	 */
	public static void main(String[] args) {
		Schema schema = new Schema();
		schema.addTable(new Table());

		XStream xstream = new XStream();
		xstream.processAnnotations(Schema.class);
		xstream.processAnnotations(Sequence.class);
		System.out.println(xstream.toXML(schema));

		File file = new File("test.xml");
		Schema s = (Schema) xstream.fromXML(file);
		System.out.println(xstream.toXML(s));
	}
}
