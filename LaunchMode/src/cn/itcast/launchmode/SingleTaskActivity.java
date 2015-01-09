package cn.itcast.launchmode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SingleTaskActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.singletask);
		
		TextView textView = (TextView) this.findViewById(R.id.singeltaskView);
        textView.setText(this.toString());
	}

	public void openMainActivity(View v){
		startActivity(new Intent(this, MainActivity.class));
	}
}
