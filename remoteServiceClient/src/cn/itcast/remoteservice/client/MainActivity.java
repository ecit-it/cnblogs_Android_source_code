package cn.itcast.remoteservice.client;

import cn.itcast.aidl.StudentQuery;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	private EditText numberText;
	private TextView resultView;
	private StudentQuery studentQuery;
	private StudentConnection conn = new StudentConnection();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		numberText = (EditText) this.findViewById(R.id.number);
		resultView = (TextView) this.findViewById(R.id.resultView);
		Intent service = new Intent("cn.itcast.student.query");
		bindService(service, conn, BIND_AUTO_CREATE);
	}

	public void queryStudent(View v) {
		String number = numberText.getText().toString();
		int num = Integer.valueOf(number);
		try {
			resultView.setText(studentQuery.queryStudent(num));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		unbindService(conn);
		super.onDestroy();
	}

	private final class StudentConnection implements ServiceConnection {
		public void onServiceConnected(ComponentName name, IBinder service) {
			studentQuery = StudentQuery.Stub.asInterface(service);
		}
		public void onServiceDisconnected(ComponentName name) {
			studentQuery = null;
		}
	}
}