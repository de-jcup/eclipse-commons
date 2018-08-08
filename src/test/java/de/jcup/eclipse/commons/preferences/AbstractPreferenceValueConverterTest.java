package de.jcup.eclipse.commons.preferences;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class AbstractPreferenceValueConverterTest {

	private TestPreferenceValueConverter converterToTest;

	@Before
	public void before(){
		converterToTest = new TestPreferenceValueConverter();
	}
	
	@Test
	public void a_list_can_be_written_to_string_and_read_back_with_same_values_as_origin(){
		TestObject obj = new TestObject();
		obj.number=10;
		obj.string="alpha";
		obj.another="beta";
		
		TestObject obj2 = new TestObject();
		obj2.number=20;
		obj2.string="gamma";
		obj2.another="delta";
		
		List<TestObject> list = new ArrayList<>();
		list.add(obj);
		list.add(obj2);
		
		/* test write to string */
		String data = converterToTest.convertListTostring(list);
		assertNotNull(data);
		
		/* test reading created string again*/
		List<TestObject> result = converterToTest.convertStringToList(data);
		
		/* test must be same values as before*/
		assertEquals(2,result.size());
		Iterator<TestObject> val = result.iterator();
		TestObject obj_ = val.next();
		assertEquals(obj.number,obj_.number);
		assertEquals(obj.string,obj_.string);
		assertEquals(obj.another,obj_.another);
	
		TestObject obj2_ = val.next();
		assertEquals(obj2.number,obj2_.number);
		assertEquals(obj2.string,obj2_.string);
		assertEquals(obj2.another,obj2_.another);
	
	}
	
	@Test
	public void standard_impl_can_convert_null_values() {
		TestObject obj = new TestObject();
		assertEquals("0,,", converterToTest.convertOneToString(obj));
	}
	
	@Test
	public void standard_impl_can_convert_values() {
		TestObject obj = new TestObject();
		obj.number=10;
		obj.string="alpha";
		obj.another="beta";
		assertEquals("10,alpha,beta", converterToTest.convertOneToString(obj));
	}
	
	
	@Test
	public void standard_impl_can_convert_from_values() {
		TestObject obj = converterToTest.convertOneFromString("10,alpha,beta");
		assertEquals(10, obj.number);
		assertEquals("alpha",obj.string);
		assertEquals("beta",obj.another);
	}
	
	@Test
	public void standard_impl_can_convert_from_values_null() {
		TestObject obj = converterToTest.convertOneFromString("0,,");
		assertEquals(0, obj.number);
		assertEquals(null,obj.string);
		assertEquals(null,obj.another);
	}

	private static class TestObject {
		private int number;
		private String string;
		private String another;
	}

	private static class TestPreferenceValueConverter extends AbstractPreferenceValueConverter<TestObject> {
		
		@Override
		protected void write(TestObject oneEntry, PreferenceDataWriter writer) {
			writer.writeInt(oneEntry.number);
			writer.writeString(oneEntry.string);
			writer.writeString(oneEntry.another);
		}
		
		@Override
		protected TestObject read(PreferenceDataReader reader) {
			TestObject oneEntry = new TestObject();
			oneEntry.number = reader.readInt();
			oneEntry.string = reader.readString();
			oneEntry.another = reader.readString();
			return oneEntry;
		}
		
	}
}
