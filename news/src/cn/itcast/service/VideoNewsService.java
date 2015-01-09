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
	 * ��ȡ���µ���Ƶ��Ѷ
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
	 * ����JSON����
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
	 * ��ȡ���µ���Ƶ��Ѷ
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
	 * �������������ص�xml����
<?xml version="1.0" encoding="UTF-8" ?> 
<videonews>
  <news id="35">
  <title>ϲ�������̫��ȫ��</title> 
  <timelength>90</timelength> 
  </news>
 <news id="12">
  <title>�������̫��</title> 
  <timelength>20</timelength> 
  </news>
 <news id="56">
  <title>�Ϸ���LILI</title> 
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
