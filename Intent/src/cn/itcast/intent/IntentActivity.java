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
    	 * (û�����ݲ����������)ֻҪIntent�е�Action��Category��������Intent-Filter�У�������֮ƥ�䣬����ƥ��ʧ��
    	 */
    	Intent intent = new Intent();//��ʽ��ͼ����Activity
    	intent.setAction("cn.itcast.zhangxx");
    	intent.addCategory("cn.itcast.category.java");   
    	intent.setDataAndType(Uri.parse("itcast://www.itcast.cn/liming"), "image/jpeg");
    	
    	startActivity(intent);//�����ڲ�ΪIntent�����android.intent.category.DEFAULT���
    }
}