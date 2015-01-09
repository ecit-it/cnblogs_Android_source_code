package cn.itcast.widgets;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.widget.RemoteViews;

public class TimerService extends Service {
	private Timer timer;
	
	@Override
	public void onCreate() {
		super.onCreate();
		timer = new Timer();
		timer.schedule(new TimeUpdateTask(), 0, 1000);
	}
	
	private final class TimeUpdateTask extends TimerTask{
		public void run() {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = dateFormat.format(new Date());
			RemoteViews remoteView = new RemoteViews(getPackageName(), R.layout.time_appwidget);
			remoteView.setTextViewText(R.id.textView, time);
			PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 100,
					new Intent(Intent.ACTION_CALL, Uri.parse("tel:133333333")), 0);
					
			remoteView.setOnClickPendingIntent(R.id.textView, pendingIntent);
			
			AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
			
			appWidgetManager.updateAppWidget(
					new ComponentName(getApplicationContext(), TimeWidgetProvider.class), remoteView);
		}		
	}

	@Override
	public void onDestroy() {
		timer.cancel();
		timer = null;
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
