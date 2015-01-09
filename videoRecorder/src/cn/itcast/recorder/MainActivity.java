package cn.itcast.recorder;

import java.io.File;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {
    private SurfaceView surfaceView;
    private RelativeLayout layout;
    private Button recordbutton;
    private Button stopbutton;
    private MediaRecorder mediaRecorder;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);
        
        surfaceView = (SurfaceView) this.findViewById(R.id.surfaceView);
        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceView.getHolder().setFixedSize(176, 144);
        surfaceView.getHolder().setKeepScreenOn(true);
        
        layout = (RelativeLayout) this.findViewById(R.id.layout);
        recordbutton = (Button) this.findViewById(R.id.recordbutton);
        stopbutton = (Button) this.findViewById(R.id.stopbutton);
    }

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			layout.setVisibility(ViewGroup.VISIBLE);
		}
		return super.onTouchEvent(event);
	}
    
    public void record(View v){
    	switch (v.getId()) {
		case R.id.recordbutton:
			try{
				File videoFile = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis()+ ".3gp");
				mediaRecorder = new MediaRecorder();
				mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
				mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
				mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
				mediaRecorder.setVideoSize(320, 240);
				mediaRecorder.setVideoFrameRate(5);
				mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
				mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
				mediaRecorder.setOutputFile(videoFile.getAbsolutePath());
				mediaRecorder.setPreviewDisplay(surfaceView.getHolder().getSurface());
				mediaRecorder.prepare();
				mediaRecorder.start();
			}catch (Exception e) {
				e.printStackTrace();
			}
			recordbutton.setEnabled(false);
			stopbutton.setEnabled(true);
			break;

		case R.id.stopbutton:
			if(mediaRecorder!=null){
				mediaRecorder.stop();
				mediaRecorder.release();
				mediaRecorder = null;
			}
			recordbutton.setEnabled(true);
			stopbutton.setEnabled(false);
			break;
		}
    }
}