package cn.itcast.remote.service;

import cn.itcast.aidl.StudentQuery;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class StudentQueryService extends Service {
	private String[] names = {"ÕÅ·É", "Àî¾²", "ÕÔŞ±"};
	private IBinder binder = new StudentQueryBinder();
	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}
	private String query(int number){
		if(number > 0 && number < 4){
			return names[number - 1];
		}
		return null;
	}
	private final class StudentQueryBinder extends StudentQuery.Stub{
		public String queryStudent(int number) throws RemoteException {			
			return query(number);
		}		
	}

}
