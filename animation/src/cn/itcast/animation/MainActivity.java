package cn.itcast.animation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class MainActivity extends Activity {
    private ViewFlipper viewFlipper;
    private float startX;
    private Animation in_lefttoright;
    private Animation out_lefttoright;
    private Animation in_righttoleft;
    private Animation out_righttoleft;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        in_lefttoright = AnimationUtils.loadAnimation(this, R.anim.enter_lefttoright);
        out_lefttoright = AnimationUtils.loadAnimation(this, R.anim.out_lefttoright);
        
        in_righttoleft = AnimationUtils.loadAnimation(this, R.anim.enter_righttoleft);
        out_righttoleft = AnimationUtils.loadAnimation(this, R.anim.out_righttoleft);
        viewFlipper = (ViewFlipper) this.findViewById(R.id.viewFlipper);
    }
    
    
    
    @Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction()==MotionEvent.ACTION_DOWN){
			startX = event.getX();
		}else if(event.getAction()==MotionEvent.ACTION_UP){
			float endX = event.getX();
			if(endX > startX){
				viewFlipper.setInAnimation(in_lefttoright);
				viewFlipper.setOutAnimation(out_lefttoright);
				viewFlipper.showNext();//显示下一页
				
			}else if(endX < startX){
				viewFlipper.setInAnimation(in_righttoleft);
				viewFlipper.setOutAnimation(out_righttoleft);
				viewFlipper.showPrevious();//显示前一页
			}
			return true;
		}
		return super.onTouchEvent(event);
	}



	public void openActivity(View v){
    	Intent intent = new Intent(this, OtherActivity.class);
    	startActivity(intent);
    	this.overridePendingTransition(R.anim.enteralpha, R.anim.outalpha);//实现Activity切换动画效果
    }
}