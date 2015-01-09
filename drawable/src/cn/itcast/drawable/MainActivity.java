package cn.itcast.drawable;

import android.app.Activity;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.LevelListDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {
    private ImageView imageView;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        imageView = (ImageView) this.findViewById(R.id.imageView);
    }
    
    public void changeImage(View v){
    	ClipDrawable clipDrawable = (ClipDrawable) imageView.getDrawable();
    	clipDrawable.setLevel(clipDrawable.getLevel()+1000);
    	
    	/*
    	TransitionDrawable transitionDrawable = (TransitionDrawable) ((Button)v).getBackground();
    	transitionDrawable.startTransition(500);
    	*/
    	
    	/*
    	LevelListDrawable levelListDrawable = (LevelListDrawable) imageView.getDrawable();
    	levelListDrawable.setLevel(12);
    	*/
    	/*
    	//LayerDrawable layerDrawable = (LayerDrawable) imageView.getDrawable();
    	LayerDrawable layerDrawable = (LayerDrawable)getResources().getDrawable(R.drawable.layerlist);
    	Drawable drawable = getResources().getDrawable(R.drawable.icon);
    	layerDrawable.setDrawableByLayerId(R.id.userimage, drawable);
    	imageView.setImageDrawable(layerDrawable);
    	*/
    }
}