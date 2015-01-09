package cn.itcast.htmlviewer;

import cn.itcast.service.PageService;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private EditText pathText;
    private TextView codeView;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        pathText = (EditText) this.findViewById(R.id.pagepath);
        codeView = (TextView) this.findViewById(R.id.codeView);
        Button button = (Button) this.findViewById(R.id.button);
        button.setOnClickListener(new ButtonClickListener());
    }
    
    private final class ButtonClickListener implements View.OnClickListener{

		public void onClick(View v) {
			String path = pathText.getText().toString();
			try{
				String html = PageService.getHtml(path);
				codeView.setText(html);
			}catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(getApplicationContext(), R.string.error, 1).show();
			}
		}
    	
    }
}