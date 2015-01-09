package cn.itcast.gesture;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
    private GestureLibrary library;
    private Gesture mgesture;
    private GestureOverlayView overlayView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        library = GestureLibraries.fromRawResource(this, R.raw.gestures);
        library.load();
        
        overlayView = (GestureOverlayView) this.findViewById(R.id.gestures);
        //只针对单笔手势：overlayView.addOnGesturePerformedListener(new GesturePerformedListener());
        overlayView.addOnGestureListener(new GestureListener());
    }
    
    public void find(View v){
    	recognize(mgesture);
    	overlayView.clear(true);
    }
    
    private final class GestureListener implements OnGestureListener{
		public void onGestureStarted(GestureOverlayView overlay, MotionEvent event) {
			Log.i(TAG, "onGestureStarted()");
		}
		public void onGesture(GestureOverlayView overlay, MotionEvent event) {
			Log.i(TAG, "onGesture()");
		}
		public void onGestureEnded(GestureOverlayView overlay, MotionEvent event) {
			Log.i(TAG, "onGestureEnded()");
			mgesture = overlay.getGesture();
		}
		public void onGestureCancelled(GestureOverlayView overlay, MotionEvent event) {
			Log.i(TAG, "onGestureCancelled()");
		}
    }
    
    private final class GesturePerformedListener implements OnGesturePerformedListener{
		public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
			recognize(gesture);
		}		
    }
    
    private void recognize(Gesture gesture) {
		ArrayList<Prediction> predictions = library.recognize(gesture);
		if(!predictions.isEmpty()){
			Prediction prediction = predictions.get(0);
			if(prediction.score >= 6){
				if("zhangxx".equals(prediction.name)){
					Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:1350505050"));
					startActivity(intent);
				}else if("close".equals(prediction.name)){
					finish();//关闭Activity
				}
			}else{
				Toast.makeText(getApplicationContext(), R.string.low, 1).show();
			}
		}else{
			Toast.makeText(getApplicationContext(), R.string.notfind, 1).show();
		}
	}
    
	@Override
	protected void onDestroy() {
		super.onDestroy();
		android.os.Process.killProcess(android.os.Process.myPid());//关闭应用
	}
    
}