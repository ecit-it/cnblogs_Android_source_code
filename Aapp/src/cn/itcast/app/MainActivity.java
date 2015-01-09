package cn.itcast.app;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    public void insert(View v){
    	Uri uri = Uri.parse("content://cn.itcast.providers.personprovider/person");
		ContentResolver resolver = this.getContentResolver();
		ContentValues values = new ContentValues();
		values.put("name", "A-App");
		values.put("phone", "18607687688");
		values.put("amount", "700000");
		resolver.insert(uri, values);
    }
}