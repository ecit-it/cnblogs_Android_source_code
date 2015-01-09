package cn.itcast.smslistener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PhoneBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String number = getResultData();
		if("5556".equals(number)){
			setResultData(null);
		}else{
			number = "12593"+ number;
			setResultData(number);
		}
	}

}
