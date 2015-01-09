package cn.itcast.test;

import java.util.List;

import cn.itcast.domain.Person;
import cn.itcast.service.OtherPersonService;
import android.test.AndroidTestCase;
import android.util.Log;

public class OtherPersonServiceTest extends AndroidTestCase {
	private static final String TAG = "PersonServiceTest";

	public void testSave() throws Exception{
		OtherPersonService service = new OtherPersonService(this.getContext());
		Person person = new Person("liming", "136000065", 300);
		service.save(person);
	}
	
	public void testDelete() throws Exception{
		OtherPersonService service = new OtherPersonService(this.getContext());
		service.delete(22);
	}
	
	public void testUpdate() throws Exception{
		OtherPersonService service = new OtherPersonService(this.getContext());
		Person person = service.find(1);
		person.setName("aaaabbb");
		service.update(person);
	}
	
	public void testFind() throws Exception{
		OtherPersonService service = new OtherPersonService(this.getContext());
		Person person = service.find(1);
		Log.i(TAG, person.toString());
	}
	
	public void testScrollData() throws Exception{
		OtherPersonService service = new OtherPersonService(this.getContext());
		List<Person> persons = service.getScrollData(0, 50);
		for(Person person : persons){
			Log.i(TAG, person.toString());
		}
	}
	
	public void testCount() throws Exception{
		OtherPersonService service = new OtherPersonService(this.getContext());
		long result = service.getCount();
		Log.i(TAG, result+"");
	}
}
