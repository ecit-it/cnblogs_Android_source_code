package cn.itcast.service;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import cn.itcast.domain.Person;

public class OtherPersonService {
	private DBOpenHelper dbOpenHelper;

	public OtherPersonService(Context context) {
		this.dbOpenHelper = new DBOpenHelper(context);
	}
	/**
	 * ��Ӽ�¼
	 * @param person
	 */
	public void save(Person person){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("name", person.getName());
		values.put("phone", person.getPhone());
		values.put("amount", person.getAmount());
		db.insert("person", null, values);
	}
	/**
	 * ɾ����¼
	 * @param id ��¼ID
	 */
	public void delete(Integer id){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.delete("person", "personid=?", new String[]{id.toString()});
	}
	/**
	 * ���¼�¼
	 * @param person
	 */
	public void update(Person person){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("name", person.getName());
		values.put("phone", person.getPhone());
		values.put("amount", person.getAmount());
		db.update("person", values, "personid=?", new String[]{person.getId().toString()});
	}
	/**
	 * ��ѯ��¼
	 * @param id ��¼ID
	 * @return
	 */
	public Person find(Integer id){
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.query("person", null, "personid=?", new String[]{id.toString()}, null, null, null);
		
		if(cursor.moveToFirst()){
			int personid = cursor.getInt(cursor.getColumnIndex("personid"));
			int amount = cursor.getInt(cursor.getColumnIndex("amount"));
			String name = cursor.getString(cursor.getColumnIndex("name"));
			String phone = cursor.getString(cursor.getColumnIndex("phone"));
			return new Person(personid, name, phone, amount);
		}
		cursor.close();
		return null;
	}
	/**
	 * ��ҳ��ȡ��¼
	 * @param offset ����ǰ���������¼
	 * @param maxResult ÿҳ��ȡ��������¼
	 * @return
	 */
	public List<Person> getScrollData(int offset, int maxResult){
		List<Person> persons = new ArrayList<Person>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.query("person", null, null, null, null, null, "personid asc", offset+ ","+ maxResult);
		
		while(cursor.moveToNext()){
			int personid = cursor.getInt(cursor.getColumnIndex("personid"));
			int amount = cursor.getInt(cursor.getColumnIndex("amount"));
			String name = cursor.getString(cursor.getColumnIndex("name"));
			String phone = cursor.getString(cursor.getColumnIndex("phone"));
			persons.add(new Person(personid, name, phone, amount));
		}
		cursor.close();
		return persons;
	}
	/**
	 * ��ȡ��¼����
	 * @return
	 */
	public long getCount(){
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.query("person", new String[]{"count(*)"}, null, null, null, null, null);
		cursor.moveToFirst();
		long result = cursor.getLong(0);
		cursor.close();
		return result;
	}
}
