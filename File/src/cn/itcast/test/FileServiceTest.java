package cn.itcast.test;

import cn.itcast.service.FileService;
import android.test.AndroidTestCase;
import android.util.Log;

public class FileServiceTest extends AndroidTestCase {
	private static final String TAG = "FileServiceTest";
	
	public void testRead() throws Throwable{
		FileService service = new FileService(this.getContext());
		String result = service.read("itcast.txt");
		Log.i(TAG, result);
	}
	
	public void testSaveAppend() throws Throwable{
		FileService service = new FileService(this.getContext());
		service.saveAppend("append.txt", ",liming");
	}
	
	public void testSaveReadable() throws Throwable{
		FileService service = new FileService(this.getContext());
		service.saveReadable("readable.txt", "readable");
	}
	
	public void testSaveWriteable() throws Throwable{
		FileService service = new FileService(this.getContext());
		service.saveWriteable("writeable.txt", "writeable");
	}
	
	public void testSaveRW() throws Throwable{
		FileService service = new FileService(this.getContext());
		service.saveRW("rw.txt", "rw");
	}
}
