package cn.itcast.launchmode;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SingleTopActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.singletop);
		
		 TextView textView = (TextView) this.findViewById(R.id.singeltopView);
	     textView.setText(this.toString());
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		Toast.makeText(this, new Date().toString(), 1).show();
		super.onNewIntent(intent);
	}

	public void openSingleTopActivity(View v){
		startActivity(new Intent(this, SingleTopActivity.class));
	}

}
