package cn.itcast.widgets;


import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

public class TimeWidgetProvider extends AppWidgetProvider {

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {//当用户从桌面上删除widgets的时候就会调用此方法
		
	}
	
	@Override
	public void onEnabled(Context context) {//第一次往桌面添加Widgets的时候才会被调用，往后再往桌面添加同类型Widgets的时候是不会被调用的
		context.startService(new Intent(context, TimerService.class));
	}



	@Override
	public void onDisabled(Context context) {//是在最后一个同类型Widgets实例被删除的时候调用
		context.stopService(new Intent(context, TimerService.class));
	}



	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		
	}

}
