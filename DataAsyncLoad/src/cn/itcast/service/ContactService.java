package cn.itcast.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.net.Uri;
import android.util.Xml;

import cn.itcast.domain.Contact;
import cn.itcast.utils.MD5;

public class ContactService {

	/**
	 * ��ȡ��ϵ��
	 * @return
	 */
	public static List<Contact> getContacts() throws Exception{
		String path = "http://192.168.1.100:8080/web/list.xml";
		HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		if(conn.getResponseCode() == 200){
			return parseXML(conn.getInputStream());
		}
		return null;
	}

	private static List<Contact> parseXML(InputStream xml) throws Exception{
		List<Contact> contacts = new ArrayList<Contact>();
		Contact contact = null;
		XmlPullParser pullParser = Xml.newPullParser();
		pullParser.setInput(xml, "UTF-8");
		int event = pullParser.getEventType();
		while(event != XmlPullParser.END_DOCUMENT){
			switch (event) {
			case XmlPullParser.START_TAG:
				if("contact".equals(pullParser.getName())){
					contact = new Contact();
					contact.id = new Integer(pullParser.getAttributeValue(0));
				}else if("name".equals(pullParser.getName())){
					contact.name = pullParser.nextText();
				}else if("image".equals(pullParser.getName())){
					contact.image = pullParser.getAttributeValue(0);
				}
				break;

			case XmlPullParser.END_TAG:
				if("contact".equals(pullParser.getName())){
					contacts.add(contact);
					contact = null;
				}
				break;
			}
			event = pullParser.next();
		}
		return contacts;
	}
	/**
	 * ��ȡ����ͼƬ,���ͼƬ�����ڻ����У��ͷ��ظ�ͼƬ������������м��ظ�ͼƬ����������
	 * @param path ͼƬ·��
	 * @return
	 */
	public static Uri getImage(String path, File cacheDir) throws Exception{// path -> MD5 ->32�ַ���.jpg
		File localFile = new File(cacheDir, MD5.getMD5(path)+ path.substring(path.lastIndexOf(".")));
		if(localFile.exists()){
			return Uri.fromFile(localFile);
		}else{
			HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			if(conn.getResponseCode() == 200){
				FileOutputStream outStream = new FileOutputStream(localFile);
				InputStream inputStream = conn.getInputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while( (len = inputStream.read(buffer)) != -1){
					outStream.write(buffer, 0, len);
				}
				inputStream.close();
				outStream.close();
				return Uri.fromFile(localFile);
			}
		}
		return null;
	}

}
