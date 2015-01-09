package cn.itcast.widgets;


import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

public class TimeWidgetProvider extends AppWidgetProvider {

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {//���û���������ɾ��widgets��ʱ��ͻ���ô˷���
		
	}
	
	@Override
	public void onEnabled(Context context) {//��һ�����������Widgets��ʱ��Żᱻ���ã����������������ͬ����Widgets��ʱ���ǲ��ᱻ���õ�
		context.startService(new Intent(context, TimerService.class));
	}



	@Override
	public void onDisabled(Context context) {//�������һ��ͬ����Widgetsʵ����ɾ����ʱ�����
		context.stopService(new Intent(context, TimerService.class));
	}



	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		
	}

}
