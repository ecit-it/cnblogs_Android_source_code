package cn.itcast.phone;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
    private EditText mobileText;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mobileText = (EditText) findViewById(R.id.mobile);
        Button button = (Button) this.findViewById(R.id.button);
        button.setOnClickListener(new ButtonClickListener());
    }
 
    private final class ButtonClickListener implements View.OnClickListener{
		public void onClick(View v) {
			String number = mobileText.getText().toString();
			Intent intent = new Intent();
			intent.setAction("android.intent.action.CALL");
			intent.setData(Uri.parse("tel:"+ number));
			startActivity(intent);//方法内部会自动为Intent添加类别：android.intent.category.DEFAULT
		}
    }
}