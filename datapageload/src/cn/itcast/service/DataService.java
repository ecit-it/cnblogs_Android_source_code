package cn.itcast.service;

import java.util.ArrayList;
import java.util.List;

public class DataService {

	public static List<String> getData(int offset, int maxResult){//��ҳ limit 0,20
		List<String> data = new ArrayList<String>();
		for(int i=0 ; i < 20 ; i++){
			data.add("ListView���ݵķ�������"+ i);
		}
		return data;
	}
}
