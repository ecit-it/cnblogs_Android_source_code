package cn.itcast.files;

import cn.itcast.service.FileService;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button button = (Button) this.findViewById(R.id.button);
        button.setOnClickListener(new ButtonClickListener());
    }
    
    private final class ButtonClickListener implements View.OnClickListener{

		public void onClick(View v) {
			EditText filenameText = (EditText) findViewById(R.id.filename);
			EditText contentText = (EditText) findViewById(R.id.filecontent);
			String filename = filenameText.getText().toString();
			String content = contentText.getText().toString();
			FileService service = new FileService(getApplicationContext());
			try {
				//判断SDCard是否存在，并且可以读写
				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
					service.saveToSDCard(filename, content);
					Toast.makeText(getApplicationContext(), R.string.success, 1).show();
				}else{
					Toast.makeText(getApplicationContext(), R.string.sdcarderror, 1).show();
				}				
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), R.string.fail, 1).show();
				e.printStackTrace();
			}
			
		}
    	
    }
}