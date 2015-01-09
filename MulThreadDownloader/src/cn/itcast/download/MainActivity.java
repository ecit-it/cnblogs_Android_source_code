package cn.itcast.download;

import java.io.File;

import cn.itcast.net.download.DownloadProgressListener;
import cn.itcast.net.download.FileDownloader;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private EditText pathText;
    private TextView resultView;
    private Button downloadButton;
    private Button stopbutton;
    private ProgressBar progressBar;
    //hanlder������������������Hander�������ڵ��߳����󶨵���Ϣ���з�����Ϣ
    private Handler handler = new UIHander();
    
    private final class UIHander extends Handler{
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				int size = msg.getData().getInt("size");
				progressBar.setProgress(size);
				float num = (float)progressBar.getProgress() / (float)progressBar.getMax();
				int result = (int)(num * 100);
				resultView.setText(result+ "%");
				if(progressBar.getProgress() == progressBar.getMax()){
					Toast.makeText(getApplicationContext(), R.string.success, 1).show();
				}
				break;

			case -1:
				Toast.makeText(getApplicationContext(), R.string.error, 1).show();
				break;
			}
		}
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        pathText = (EditText) this.findViewById(R.id.path);
        resultView = (TextView) this.findViewById(R.id.resultView);
        downloadButton = (Button) this.findViewById(R.id.downloadbutton);
        stopbutton = (Button) this.findViewById(R.id.stopbutton);
        progressBar = (ProgressBar) this.findViewById(R.id.progressBar);
        ButtonClickListener listener = new ButtonClickListener();
        downloadButton.setOnClickListener(listener);
        stopbutton.setOnClickListener(listener);
    }
    
    private final class ButtonClickListener implements View.OnClickListener{
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.downloadbutton:
				String path = pathText.getText().toString();
				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
					File saveDir = Environment.getExternalStorageDirectory();
					download(path, saveDir);
				}else{
					Toast.makeText(getApplicationContext(), R.string.sdcarderror, 1).show();
				}
				downloadButton.setEnabled(false);
				stopbutton.setEnabled(true);
				break;

			case R.id.stopbutton:
				exit();
				downloadButton.setEnabled(true);
				stopbutton.setEnabled(false);
				break;
			}
		}
		/*
		�����û��������¼�(���button, ������Ļ....)�������̸߳�����ģ�������̴߳��ڹ���״̬��
		��ʱ�û������������¼����û����5���ڵõ�����ϵͳ�ͻᱨ��Ӧ������Ӧ������
		���������߳��ﲻ��ִ��һ���ȽϺ�ʱ�Ĺ���������������߳��������޷������û��������¼���
		���¡�Ӧ������Ӧ������ĳ��֡���ʱ�Ĺ���Ӧ�������߳���ִ�С�
		 */
		private DownloadTask task;
		/**
		 * �˳�����
		 */
		public void exit(){
			if(task!=null) task.exit();
		}
		private void download(String path, File saveDir) {//���������߳�
			task = new DownloadTask(path, saveDir);
			new Thread(task).start();
		}
		
		
		/*
		 * UI�ؼ�������ػ�(����)�������̸߳�����ģ���������߳��и���UI�ؼ���ֵ�����º��ֵ�����ػ浽��Ļ��
		 * һ��Ҫ�����߳������UI�ؼ���ֵ��������������Ļ����ʾ���������������߳��и���UI�ؼ���ֵ
		 */
		private final class DownloadTask implements Runnable{
			private String path;
			private File saveDir;
			private FileDownloader loader;
			public DownloadTask(String path, File saveDir) {
				this.path = path;
				this.saveDir = saveDir;
			}
			/**
			 * �˳�����
			 */
			public void exit(){
				if(loader!=null) loader.exit();
			}
			
			public void run() {
				try {
					loader = new FileDownloader(getApplicationContext(), path, saveDir, 3);
					progressBar.setMax(loader.getFileSize());//���ý����������̶�
					loader.download(new DownloadProgressListener() {
						public void onDownloadSize(int size) {
							Message msg = new Message();
							msg.what = 1;
							msg.getData().putInt("size", size);
							handler.sendMessage(msg);
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
					handler.sendMessage(handler.obtainMessage(-1));
				}
			}			
		}
    }
    
    
}