package cn.itcast.test;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.itcast.utils.StreamTool;

import android.test.AndroidTestCase;

public class XMLTest extends AndroidTestCase {

	public void testSendXML() throws Exception{
		InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("person.xml");
		byte[] data = StreamTool.read(inStream);
		String path = "http://192.168.1.100:8080/web/XmlServlet";
		HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
		conn.setRequestProperty("Content-Length", String.valueOf(data.length));
		conn.getOutputStream().write(data);
		if(conn.getResponseCode() == 200){
			System.out.println("发送成功");
		}else{
			System.out.println("发送失败");
		}
	}
}
