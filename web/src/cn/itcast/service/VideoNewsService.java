package cn.itcast.service;

import java.util.List;

import cn.itcast.domain.News;

public interface VideoNewsService {
	/**
	 * 获取最新的视频资讯
	 * @return
	 */
	public List<News> getLastNews();

}