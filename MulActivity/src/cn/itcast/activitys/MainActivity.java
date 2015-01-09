package cn.itcast.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    public void openActivity(View v){
    	//���µ�Activity
    	Intent intent = new Intent(this, OtherActivity.class);//�������,��ʾ��ͼ:��ȷָ����������Ƶ���ͼ����ʾ��ͼ
    	//����ָ��������ƣ����кܶ�д��
    	//1> intent.setClass(this, OtherActivity.class);//ָ��Ҫ������������
    	//2> intent.setClassName(this, "cn.itcast.activitys.OtherActivity");
    	//3> intent.setComponent(new ComponentName(this, OtherActivity.class));
    	/*
    	intent.putExtra("company", "���ǲ���");
    	intent.putExtra("age", 5);
    	*/
    	Bundle bundle = new Bundle();
    	bundle.putString("company", "CSDN");
    	bundle.putInt("age", 11);
    	intent.putExtras(bundle);
    	
    	//startActivity(intent);
    	startActivityForResult(intent, 100);
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		String result = data.getStringExtra("result");
		Toast.makeText(getApplicationContext(),result, 1).show();
		
		super.onActivityResult(requestCode, resultCode, data);
	}
    
    
}