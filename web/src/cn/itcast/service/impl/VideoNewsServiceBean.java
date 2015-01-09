package cn.itcast.service.impl;

import java.util.ArrayList;
import java.util.List;

import cn.itcast.domain.News;
import cn.itcast.service.VideoNewsService;

public class VideoNewsServiceBean implements VideoNewsService {

	public List<News> getLastNews(){
		List<News> newes = new ArrayList<News>();
		newes.add(new News(90, "Ï²ÑòÑòÓë»ÒÌ«ÀÇÈ«¼¯", 78));
		newes.add(new News(10, "ÊµÅÄ½¢ÔØÖ±Éı¶«º£¾ÈÔ®ÑİÏ°", 28));
		newes.add(new News(56, "¿¦ÂóÂ¡VSºÉÀ¼", 70));
		return newes;
	}
}
