package cn.itcast.phonelistener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent service = new Intent(context, PhoneService.class);//��ʽ/��ʽ
		context.startService(service);//Intent�������(Service)
	}

}
