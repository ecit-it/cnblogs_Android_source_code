package cn.itcast.frame;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.MessageQueue;
import android.widget.TextView;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        TextView textView = (TextView) this.findViewById(R.id.textView);
        textView.setBackgroundResource(R.drawable.frame);//绑定Frame动画图形
        final AnimationDrawable drawable = (AnimationDrawable) textView.getBackground();
        
        getMainLooper().myQueue().addIdleHandler(new MessageQueue.IdleHandler() {			
			public boolean queueIdle() {
				drawable.start();//启动动画
				return false;
			}
		});
        
    }
}