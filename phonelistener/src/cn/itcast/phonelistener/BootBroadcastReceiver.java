package cn.itcast.phonelistener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent service = new Intent(context, PhoneService.class);//显式/隐式
		context.startService(service);//Intent激活组件(Service)
	}

}
