package cn.itcast.launchmode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        TextView textView = (TextView) this.findViewById(R.id.textView);
        textView.setText(this.toString());
    }
    
    public void openStandardActivity(View v){
    	startActivity(new Intent(this, MainActivity.class));
    }
    
    public void openSingleTopActivity(View v){
    	startActivity(new Intent(this, SingleTopActivity.class));
    }
    
    public void openSingleTaskActivity(View v){
    	startActivity(new Intent(this, SingleTaskActivity.class));
    }
    
    public void openSingleIntanceActivity(View v){
    	Intent intent = new Intent();
    	intent.setClassName("cn.itcast.singleinstance", "cn.itcast.singleinstance.SingleInstanceActivity");
    	startActivity(intent);
    }
    
}