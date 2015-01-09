package cn.itcast.test;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

public class AccessContentProviderTest extends AndroidTestCase {
	private static final String TAG = "AccessContentProviderTest";
	
	public void testInsert() throws Exception{		                               
		Uri uri = Uri.parse("content://cn.itcast.providers.personprovider/person");
		ContentResolver resolver = this.getContext().getContentResolver();
		ContentValues values = new ContentValues();
		values.put("name", "laoli");
		values.put("phone", "1860103838383");
		values.put("amount", "50000000000");
		resolver.insert(uri, values);
	}
	
	public void testDelete() throws Exception{
		Uri uri = Uri.parse("content://cn.itcast.providers.personprovider/person/20");
		ContentResolver resolver = this.getContext().getContentResolver();
		resolver.delete(uri, null, null);
	}
	
	public void testUpdate() throws Exception{
		Uri uri = Uri.parse("content://cn.itcast.providers.personprovider/person/1");
		ContentResolver resolver = this.getContext().getContentResolver();
		ContentValues values = new ContentValues();
		values.put("name", "zhangxiaoxiao");
		resolver.update(uri, values, null, null);
	}
	
	public void testQuery() throws Exception{
		Uri uri = Uri.parse("content://cn.itcast.providers.personprovider/person");
		ContentResolver resolver = this.getContext().getContentResolver();
		Cursor cursor = resolver.query(uri, null, null, null, "personid asc");
		while(cursor.moveToNext()){
			String name = cursor.getString(cursor.getColumnIndex("name"));
			Log.i(TAG, name);
		}
		cursor.close();
	}
}
