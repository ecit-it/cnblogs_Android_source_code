package cn.itcast.service;

import java.util.List;

import cn.itcast.domain.News;

public interface VideoNewsService {
	/**
	 * ��ȡ���µ���Ƶ��Ѷ
	 * @return
	 */
	public List<News> getLastNews();

}