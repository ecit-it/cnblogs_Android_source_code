package cn.itcast.service;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferencesService {
	private Context context;
	
	public PreferencesService(Context context) {
		this.context = context;
	}
	/**
	 * 保存参数
	 * @param name 姓名
	 * @param age 年龄
	 */
	public void save(String name, Integer age) {
		SharedPreferences preferences = context.getSharedPreferences("itcast", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("name", name);
		editor.putInt("age", age);
		editor.commit();
	}
	/**
	 * 获取各项配置参数
	 * @return
	 */
	public Map<String, String> getPreferences(){
		Map<String, String> params = new HashMap<String, String>();
		SharedPreferences preferences = context.getSharedPreferences("itcast", Context.MODE_PRIVATE);
		params.put("name", preferences.getString("name", ""));
		params.put("age", String.valueOf(preferences.getInt("age", 0)));
		return params;
	}
}
