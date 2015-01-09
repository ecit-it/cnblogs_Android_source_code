package cn.itcast.tabhost;

import android.app.Activity;
import android.os.Bundle;
import android.os.Debug;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class MainActivity extends Activity {
	TabHost tabHost;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
       
        Debug.startMethodTracing("itcast");

        tabHost = (TabHost) this.findViewById(R.id.tabhost);
        tabHost.setup();
        
        TabSpec tabSpec = tabHost.newTabSpec("page1");
        //tabSpec.setIndicator("首页", getResources().getDrawable(R.drawable.i1));
        tabSpec.setIndicator(createTabView("首页"));
        tabSpec.setContent(R.id.page1);
        tabHost.addTab(tabSpec);
        
        tabSpec = tabHost.newTabSpec("page2");
       // tabSpec.setIndicator("第二页", getResources().getDrawable(R.drawable.i2));
        tabSpec.setIndicator(createTabView("第二页"));
        tabSpec.setContent(R.id.page2);
        tabHost.addTab(tabSpec);
        
        tabSpec = tabHost.newTabSpec("page3");
        //tabSpec.setIndicator("第三页", getResources().getDrawable(R.drawable.i7));
        tabSpec.setIndicator(createTabView("第三页"));
        tabSpec.setContent(R.id.page3);
        tabHost.addTab(tabSpec);
        
        tabHost.setCurrentTab(0);
    }
    
	@Override
	protected void onDestroy() {
		Debug.stopMethodTracing();
		super.onDestroy();
	}

	private View createTabView(String name) {
		//View tabView = getLayoutInflater().inflate(R.layout.tab, null);
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setBackgroundColor(0xFFFFFF);
		
		TextView textView = new TextView(this);
		textView.setText(name);
		textView.setBackgroundResource(R.drawable.tab_bg);
		textView.setTextColor(0xFFFFFF);
		textView.setTextSize(18.0f);
		textView.setGravity(Gravity.CENTER);
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		linearLayout.addView(textView, params);
		
		return linearLayout;
	}
}