package cn.itcast.manage;

import java.io.File;

import cn.itcast.service.NewsService;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
    private EditText titleText;
    private EditText lengthText;
    private EditText nameText;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        titleText = (EditText) this.findViewById(R.id.title);
        lengthText = (EditText) this.findViewById(R.id.timelength);
        nameText = (EditText) this.findViewById(R.id.filename);
    }
    
    public void save(View v){
    	String filename = nameText.getText().toString();
    	String title = titleText.getText().toString();
    	String length = lengthText.getText().toString();
    	if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ||
    			Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY) ){
    		File uploadFile = new File(Environment.getExternalStorageDirectory(), filename);
    		if(uploadFile.exists()){
    			boolean result = NewsService.save(title, length, uploadFile);
    			if(result){
            		Toast.makeText(getApplicationContext(), R.string.success, 1).show();
            	}else{
            		Toast.makeText(getApplicationContext(), R.string.error, 1).show();
            	}
    		}else{
    			Toast.makeText(getApplicationContext(), R.string.filenoexsit, 1).show();
    		}
    	}else{
    		boolean result = NewsService.save(title, length);
        	if(result){
        		Toast.makeText(getApplicationContext(), R.string.success, 1).show();
        	}else{
        		Toast.makeText(getApplicationContext(), R.string.error, 1).show();
        	}
    	}
    }
}