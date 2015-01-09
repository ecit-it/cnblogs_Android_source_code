package cn.itcast.settings;

import java.util.Map;

import cn.itcast.service.PreferencesService;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
    private EditText nameText;
    private EditText ageText;
    private PreferencesService service;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        nameText = (EditText) this.findViewById(R.id.name);
        ageText = (EditText) this.findViewById(R.id.age);
        service = new PreferencesService(this);
        Map<String, String> params = service.getPreferences();
        nameText.setText(params.get("name"));
        ageText.setText(params.get("age"));
    }
    
    public void save(View v){
    	String name = nameText.getText().toString();
    	String age = ageText.getText().toString();
    	service.save(name, Integer.valueOf(age));
    	Toast.makeText(getApplicationContext(), R.string.success, 1).show();
    }
    
    
}