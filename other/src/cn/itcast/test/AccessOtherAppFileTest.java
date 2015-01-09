package cn.itcast.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.test.AndroidTestCase;
import android.util.Log;

public class AccessOtherAppFileTest extends AndroidTestCase {

	private static String read(InputStream inStream) throws Exception{
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while((len = inStream.read(buffer)) != -1){
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		return new String(data);
	}
	
	public void testAccessFile() throws Throwable{
		String path = "/data/data/cn.itcast.files/files/itcast.txt";
		File file = new File(path);
		FileInputStream inStream = new FileInputStream(file);
		Log.i("AccessOtherAppFileTest", read(inStream));
	}
	
	public void testAccessFile2() throws Throwable{
		String path = "/data/data/cn.itcast.files/files/append.txt";
		File file = new File(path);
		FileInputStream inStream = new FileInputStream(file);
		Log.i("AccessOtherAppFileTest", read(inStream));
	}
	
	public void testAccessFile3() throws Throwable{
		String path = "/data/data/cn.itcast.files/files/readable.txt";
		File file = new File(path);
		FileInputStream inStream = new FileInputStream(file);
		Log.i("AccessOtherAppFileTest", read(inStream));
	}
	
	public void testAccessFile4() throws Throwable{
		String path = "/data/data/cn.itcast.files/files/writeable.txt";
		File file = new File(path);
		FileInputStream inStream = new FileInputStream(file);
		Log.i("AccessOtherAppFileTest", read(inStream));
	}
	
	public void testAccessFile5() throws Throwable{
		String path = "/data/data/cn.itcast.files/files/writeable.txt";
		File file = new File(path);
		FileOutputStream outStream = new FileOutputStream(file);
		outStream.write("itcast".getBytes());
		outStream.close();
	}
	
	public void testAccessFile7() throws Throwable{
		String path = "/data/data/cn.itcast.files/files/rw.txt";
		File file = new File(path);
		FileInputStream inStream = new FileInputStream(file);
		Log.i("AccessOtherAppFileTest", read(inStream));
	}
	
	public void testAccessFile8() throws Throwable{
		String path = "/data/data/cn.itcast.files/files/rw.txt";
		File file = new File(path);
		FileOutputStream outStream = new FileOutputStream(file);
		outStream.write("8888".getBytes());
		outStream.close();
	}
}
