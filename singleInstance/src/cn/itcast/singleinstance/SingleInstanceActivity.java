package cn.itcast.singleinstance;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class SingleInstanceActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        TextView textView = (TextView) this.findViewById(R.id.textView);
        textView.setText(this.toString());
    }
}