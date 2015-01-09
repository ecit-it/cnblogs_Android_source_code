package cn.itcast.picture;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {
    private View layout;
    private Camera camera;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);
        
        layout = this.findViewById(R.id.buttonlayout);
        
        SurfaceView surfaceView = (SurfaceView) this.findViewById(R.id.surfaceView);
        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceView.getHolder().setFixedSize(176, 144);
        surfaceView.getHolder().setKeepScreenOn(true);
        surfaceView.getHolder().addCallback(new SurfaceCallback());
    }
    
    public void takepicture(View v){
    	if(camera!=null){
    		switch (v.getId()) {
    		case R.id.takepicture:
    			camera.takePicture(null, null, new MyPictureCallback());
    			break;

    		case R.id.autofocus:
    			camera.autoFocus(null);
    			break;
    		}
    	}
    }
    
    private final class MyPictureCallback implements PictureCallback{
		public void onPictureTaken(byte[] data, Camera camera) {
			try {
				File jpgFile = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis()+".jpg");
				FileOutputStream outStream = new FileOutputStream(jpgFile);
				outStream.write(data);
				outStream.close();
				camera.startPreview();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
    	
    }
    
    private final class SurfaceCallback implements Callback{
		public void surfaceCreated(SurfaceHolder holder) {
			try{
				camera = Camera.open();//打开摄像头
				Camera.Parameters parameters = camera.getParameters();
				//Log.i("MainActivity", parameters.flatten());
				parameters.setPreviewSize(800, 480);
				parameters.setPreviewFrameRate(5);
				parameters.setPictureSize(1024,768);
				parameters.setJpegQuality(80);
				camera.setParameters(parameters);
				camera.setPreviewDisplay(holder);
				camera.startPreview();//开始预览
			}catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			if(camera!=null){
				camera.release();
				camera = null;
			}
		}
    	
    }

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			layout.setVisibility(ViewGroup.VISIBLE);
			return true;
		}
		return super.onTouchEvent(event);
	}
    
    
}