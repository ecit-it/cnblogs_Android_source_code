package cn.itcast.student;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class StudentService extends Service{
	private String[] names = {"ÕÅ·É","ÀîÐ¡Áú","ÕÔÞ±"};
	private IBinder binder = new StundentBinder();
	
	public String query(int no){
		if(no>0 && no<4){
			return names[no - 1];
		}
		return null;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}
	
	private class StundentBinder extends Binder implements IStundent{
		public String queryStudent(int no) {
			return query(no);
		}
	}

}
