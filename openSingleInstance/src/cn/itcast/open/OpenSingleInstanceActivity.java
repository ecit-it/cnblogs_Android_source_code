package cn.itcast.open;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class OpenSingleInstanceActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    public void openSingleIntanceActivity(View v){
    	Intent intent = new Intent();
    	intent.setClassName("cn.itcast.singleinstance", "cn.itcast.singleinstance.SingleInstanceActivity");
    	startActivity(intent);
    }
}