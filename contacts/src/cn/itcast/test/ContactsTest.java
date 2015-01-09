package cn.itcast.test;

import java.util.ArrayList;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

public class ContactsTest extends AndroidTestCase {
	private static final String TAG = "ContactsTest";
	//获取所有联系人
	public void testContacts() throws Exception{
		Uri uri = Uri.parse("content://com.android.contacts/contacts");
		ContentResolver resolver = getContext().getContentResolver();
		Cursor cursor = resolver.query(uri, new String[]{"_id"}, null, null, null);
		while(cursor.moveToNext()){
			int contactid = cursor.getInt(0);
			StringBuilder sb = new StringBuilder("contactid=");
			sb.append(contactid);
			uri = Uri.parse("content://com.android.contacts/contacts/"+ contactid+ "/data");
			Cursor datacursor = resolver.query(uri, new String[]{"mimetype","data1","data2"}, null, null, null);
			while(datacursor.moveToNext()){
				String data = datacursor.getString(datacursor.getColumnIndex("data1"));
				String type = datacursor.getString(datacursor.getColumnIndex("mimetype"));
				if("vnd.android.cursor.item/name".equals(type)){//姓名
					sb.append(",name="+ data);
				}else if("vnd.android.cursor.item/email_v2".equals(type)){//email
					sb.append(",email="+ data);
				}else if("vnd.android.cursor.item/phone_v2".equals(type)){//phone
					sb.append(",phone="+ data);
				}
			}
			Log.i(TAG, sb.toString());
		}
	}
	//根据号码获取联系人的姓名
	public void testContactNameByNumber() throws Exception{
		String number = "18601025011";
		Uri uri = Uri.parse("content://com.android.contacts/data/phones/filter/"+ number);
		ContentResolver resolver = getContext().getContentResolver();
		Cursor cursor = resolver.query(uri, new String[]{"display_name"}, null, null, null);
		if(cursor.moveToFirst()){
			String name = cursor.getString(0);
			Log.i(TAG, name);
		}
		cursor.close();
	}
	//添加联系人
	public void testAddContact() throws Exception{
		Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
		ContentResolver resolver = getContext().getContentResolver();
		ContentValues values = new ContentValues();
		long contactid = ContentUris.parseId(resolver.insert(uri, values));
		//添加姓名
		uri = Uri.parse("content://com.android.contacts/data");
		values.put("raw_contact_id", contactid);
		values.put("mimetype", "vnd.android.cursor.item/name");
		values.put("data2", "张小小");
		resolver.insert(uri, values);
		//添加电话
		values.clear();
		values.put("raw_contact_id", contactid);
		values.put("mimetype", "vnd.android.cursor.item/phone_v2");
		values.put("data2", "2");
		values.put("data1", "13671323507");
		resolver.insert(uri, values);
		
		//添加Email
		values.clear();
		values.put("raw_contact_id", contactid);
		values.put("mimetype", "vnd.android.cursor.item/email_v2");
		values.put("data2", "2");
		values.put("data1", "zhangxx@csdn.net");
		resolver.insert(uri, values);
	}
	//在同一个事务中完成联系人各项数据的添加
	public void testAddContact2() throws Exception{
		Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
		ContentResolver resolver = getContext().getContentResolver();
		ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();
		ContentProviderOperation op1 = ContentProviderOperation.newInsert(uri)
			.withValue("account_name", null)
			.build();
		operations.add(op1);
		
		uri = Uri.parse("content://com.android.contacts/data");
		ContentProviderOperation op2 = ContentProviderOperation.newInsert(uri)
			.withValueBackReference("raw_contact_id", 0)
			.withValue("mimetype", "vnd.android.cursor.item/name")
			.withValue("data2", "李小龙")
			.build();
		operations.add(op2);
		
		ContentProviderOperation op3 = ContentProviderOperation.newInsert(uri)
			.withValueBackReference("raw_contact_id", 0)
			.withValue("mimetype", "vnd.android.cursor.item/phone_v2")
			.withValue("data1", "13560650505")
			.withValue("data2", "2")
			.build();
		operations.add(op3);
		
		ContentProviderOperation op4 = ContentProviderOperation.newInsert(uri)
			.withValueBackReference("raw_contact_id", 0)
			.withValue("mimetype", "vnd.android.cursor.item/email_v2")
			.withValue("data1", "liming@sohu.com")
			.withValue("data2", "2")
			.build();
		operations.add(op4);
		
		resolver.applyBatch("com.android.contacts", operations);
	}
	
}
