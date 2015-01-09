package cn.itcast.mp3;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
    private EditText nameText;
    private String path;
    private MediaPlayer mediaPlayer;
    private boolean pause;
    private int position;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mediaPlayer = new MediaPlayer();
        nameText = (EditText) this.findViewById(R.id.filename);
        
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(new MyPhoneListener(), PhoneStateListener.LISTEN_CALL_STATE);
    }
    
    private final class MyPhoneListener extends PhoneStateListener{
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING://来电
				if(mediaPlayer.isPlaying()) {
					position = mediaPlayer.getCurrentPosition();
					mediaPlayer.stop();
				}
				break;

			case TelephonyManager.CALL_STATE_IDLE:
				if(position>0 && path!=null){
					play(position);
					position = 0;
				}
				break;
			}
		}
    }
    /*
    @Override
	protected void onPause() {
		if(mediaPlayer.isPlaying()) {
			position = mediaPlayer.getCurrentPosition();
			mediaPlayer.stop();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		if(position>0 && path!=null){
			play(position);
			position = 0;
		}
		super.onResume();
	}
*/
	@Override
	protected void onDestroy() {
    	mediaPlayer.release();
    	mediaPlayer = null;
		super.onDestroy();
	}

	public void mediaplay(View v){
    	switch (v.getId()) {
		case R.id.playbutton:
			String filename = nameText.getText().toString();
			File audio = new File(Environment.getExternalStorageDirectory(), filename);
			if(audio.exists()){
				path = audio.getAbsolutePath();
				play(0);
			}else{
				path = null;
				Toast.makeText(getApplicationContext(), R.string.filenoexist, 1).show();
			}
			break;
			
		case R.id.pausebutton:
			if(mediaPlayer.isPlaying()){
				mediaPlayer.pause();//暂停
				pause = true;
				((Button)v).setText(R.string.continues);
			}else{
				if(pause){
					mediaPlayer.start();//继续播放
					pause = false;
					((Button)v).setText(R.string.pausebutton);
				}
			}
			break;
		case R.id.resetbutton:
			if(mediaPlayer.isPlaying()){
				mediaPlayer.seekTo(0);//从开始位置播放音乐
			}else{
				if(path!=null){
					play(0);
				}
			}
			break;
		case R.id.stopbutton:	
			if(mediaPlayer.isPlaying()) mediaPlayer.stop();
			break;
		}
    }

	private void play(int position) {
		try {
			mediaPlayer.reset();//把各项参数恢复到初始状态
			mediaPlayer.setDataSource(path);
			mediaPlayer.prepare();//进行缓冲
			mediaPlayer.setOnPreparedListener(new PrepareListener(position));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private final class PrepareListener implements OnPreparedListener{
		private int position;
		public PrepareListener(int position) {
			this.position = position;
		}

		public void onPrepared(MediaPlayer mp) {
			mediaPlayer.start();//开始播放
			if(position>0) mediaPlayer.seekTo(position);
		}
	}
}