package cn.itcast.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.itcast.domain.Person;
import cn.itcast.service.PersonService;
import android.test.AndroidTestCase;
import android.util.Log;

public class PersonServiceTest extends AndroidTestCase {
	private static final String TAG = "PersonServiceTest";
	
	public void testPersons() throws Exception{
		InputStream xml = this.getClass().getClassLoader().getResourceAsStream("person.xml");
		List<Person> persons = PersonService.getPersons(xml);
		for(Person person : persons){
			Log.i(TAG, person.toString());
		}
	}
	
	public void testSave() throws Exception{
		List<Person> persons = new ArrayList<Person>();
		persons.add(new Person(43, "zhangxx", 80));
		persons.add(new Person(12, "lili", 20));
		persons.add(new Person(78, "xiaoxiao", 8));
		// <°ü>/files
		File xmlFile = new File(getContext().getFilesDir(), "itcast.xml");
		FileOutputStream outStream = new FileOutputStream(xmlFile);
		PersonService.save(persons, outStream);
	}
}
