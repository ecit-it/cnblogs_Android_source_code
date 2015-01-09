package cn.itcast.sms;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
    private EditText numberText;
    private EditText contentText;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        numberText = (EditText) this.findViewById(R.id.number);
        contentText = (EditText) this.findViewById(R.id.content);
        Button button = (Button) this.findViewById(R.id.button);
        button.setOnClickListener(new ButtonClickListener());
    }
    
    private final class ButtonClickListener implements View.OnClickListener{

		public void onClick(View v) {
			String number = numberText.getText().toString();
			String content = contentText.getText().toString();
			SmsManager manager = SmsManager.getDefault();
			ArrayList<String> texts = manager.divideMessage(content);
			for(String text : texts){
				manager.sendTextMessage(number, null, text, null, null);
			}
			Toast.makeText(MainActivity.this, R.string.success, Toast.LENGTH_LONG).show();
		}
    	
    }
}