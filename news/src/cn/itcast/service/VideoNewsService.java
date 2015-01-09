package cn.itcast.service;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import cn.itcast.domain.News;
import cn.itcast.utils.StreamTool;

public class VideoNewsService {
	/**
	 * 获取最新的视频资讯
	 * @return
	 * @throws Exception
	 */
	public static List<News> getJSONLastNews() throws Exception{
		String path = "http://192.168.0.168:8080/web/ListServlet?format=json";
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		if(conn.getResponseCode() == 200){
			InputStream inStream = conn.getInputStream();
			return parseJSON(inStream);
		}
		return null;
	}
	/**
	 * 解析JSON数据
	 * @param inStream
	 * @return
	 */
	private static List<News> parseJSON(InputStream inStream) throws Exception{
		List<News> newses = new ArrayList<News>();
		byte[] data = StreamTool.read(inStream);
		String json = new String(data);
		JSONArray array = new JSONArray(json);
		for(int i = 0 ; i < array.length() ; i++){
			JSONObject jsonObject = array.getJSONObject(i);
			News news = new News(jsonObject.getInt("id"), jsonObject.getString("title"), jsonObject.getInt("timelength"));
			newses.add(news);
		}
		return newses;
	}

	/**
	 * 获取最新的视频资讯
	 * @return
	 * @throws Exception
	 */
	public static List<News> getLastNews() throws Exception{
		String path = "http://192.168.0.168:8080/web/ListServlet";
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		if(conn.getResponseCode() == 200){
			InputStream inStream = conn.getInputStream();
			return parseXML(inStream);
		}
		return null;
	}
	/**
	 * 解析服务器返回的xml数据
<?xml version="1.0" encoding="UTF-8" ?> 
<videonews>
  <news id="35">
  <title>喜羊羊与灰太狼全集</title> 
  <timelength>90</timelength> 
  </news>
 <news id="12">
  <title>老张与灰太狼</title> 
  <timelength>20</timelength> 
  </news>
 <news id="56">
  <title>老方与LILI</title> 
  <timelength>30</timelength> 
  </news>
</videonews>
	 * @param inStream
	 * @return
	 */
	private static List<News> parseXML(InputStream inStream) throws Exception {
		List<News> newses = new ArrayList<News>();
		News news = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int event = parser.getEventType();
		while( event != XmlPullParser.END_DOCUMENT){
			switch (event) {
			case XmlPullParser.START_TAG:
				if("news".equals(parser.getName())){
					int id = new Integer(parser.getAttributeValue(0));
					news = new News();
					news.setId(id);
				}else if("title".equals(parser.getName())){
					news.setTitle(parser.nextText());
				}else if("timelength".equals(parser.getName())){
					news.setTimelength(new Integer(parser.nextText()));
				}
				break;

			case XmlPullParser.END_TAG:
				if("news".equals(parser.getName())){
					newses.add(news);
					news = null;
				}
				break;
			}
			event = parser.next();
		}
		return newses;
	}
}
