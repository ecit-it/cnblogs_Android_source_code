package cn.itcast.service.impl;

import java.util.ArrayList;
import java.util.List;

import cn.itcast.domain.News;
import cn.itcast.service.VideoNewsService;

public class VideoNewsServiceBean implements VideoNewsService {

	public List<News> getLastNews(){
		List<News> newes = new ArrayList<News>();
		newes.add(new News(90, "ϲ�������̫��ȫ��", 78));
		newes.add(new News(10, "ʵ�Ľ���ֱ��������Ԯ��ϰ", 28));
		newes.add(new News(56, "����¡VS����", 70));
		return newes;
	}
}
