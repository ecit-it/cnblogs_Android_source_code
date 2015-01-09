package cn.itcast.test;

import android.test.AndroidTestCase;
import android.util.Log;

public class LogTest extends AndroidTestCase {
	private static final String TAG = "LogTest";
	
	public void testOutLog() throws Throwable{
		Log.i(TAG, "www.itcast.cn");
	}
	
	public void testOutLog5() throws Throwable{
		Log.i(TAG, "´«ÖÇ²¥¿Í");
	}
	
	public void testOutLog2() throws Throwable{
		System.out.println("www.csdn.cn");
	}
	
	public void testOutLog3() throws Throwable{
		System.err.println("www.sohu.cn");
	}
}
