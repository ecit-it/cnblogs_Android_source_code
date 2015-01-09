package cn.itcast.metadata;

import android.app.Activity;
import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        try {
        	ActivityInfo activityInfo = this.getPackageManager().getActivityInfo(
					new ComponentName(this, MainActivity.class), PackageManager.GET_META_DATA);
        	Bundle bundle = activityInfo.metaData;
        	String name = bundle.getString("cn.itcast.name");
        	String app = bundle.getString("cn.itcast.app");
        	int age = bundle.getInt("cn.itcast.age");
        	int sourceid = bundle.getInt("cn.itcast.id");
        	
        	Toast.makeText(this, "name="+ name+ ",age="+ age
        			+ ",app="+ app+ ",sourceid="+ sourceid, 1).show();
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}