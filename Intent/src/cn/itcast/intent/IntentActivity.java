package cn.itcast.intent;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class IntentActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    public void openActivity(View v){
    	/**
    	 * (没设数据参数的情况下)只要Intent中的Action和Category都出现在Intent-Filter中，就能与之匹配，否则匹配失败
    	 */
    	Intent intent = new Intent();//隐式意图激活Activity
    	intent.setAction("cn.itcast.zhangxx");
    	intent.addCategory("cn.itcast.category.java");   
    	intent.setDataAndType(Uri.parse("itcast://www.itcast.cn/liming"), "image/jpeg");
    	
    	startActivity(intent);//方法内部为Intent添加了android.intent.category.DEFAULT类别
    }
}