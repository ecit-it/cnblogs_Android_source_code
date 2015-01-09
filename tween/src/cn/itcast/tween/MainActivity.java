package cn.itcast.tween;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
       // Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);//使用alpha.xml生成动画效果对象
       // Animation animation = AnimationUtils.loadAnimation(this, R.anim.translate);
       // Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale);
       // Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        
       /* Animation animation = new RotateAnimation(0, 360, 
        		Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(5000);*/
        
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.itcast);
        
        animation.setFillAfter(true);
        ImageView imageView = (ImageView) this.findViewById(R.id.imageView);
        imageView.startAnimation(animation);
    }
}