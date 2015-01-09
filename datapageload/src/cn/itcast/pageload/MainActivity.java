package cn.itcast.pageload;

import java.util.ArrayList;
import java.util.List;

import cn.itcast.service.DataService;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {
    private ListView listView;
    private List<String> data = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    View footer;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        footer = getLayoutInflater().inflate(R.layout.footer, null);
        
        listView = (ListView) this.findViewById(R.id.listView);
        listView.setOnScrollListener(new ScrollListener());
        
        data.addAll(DataService.getData(0, 20));        
        adapter = new ArrayAdapter<String>(this, R.layout.listview_item, R.id.textView, data);
        listView.addFooterView(footer);//���ҳ��(����ListView���)
        listView.setAdapter(adapter);
        listView.removeFooterView(footer);
    }
    
    private int number = 20;//ÿ�λ�ȡ����������
    private int maxpage = 5;//�ܹ��ж���ҳ
    private boolean loadfinish = true;
    private final class ScrollListener implements OnScrollListener{
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			Log.i("MainActivity", "onScrollStateChanged(scrollState="+ scrollState+ ")");
		}
		
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			Log.i("MainActivity", "onScroll(firstVisibleItem="+ firstVisibleItem+ ",visibleItemCount="+
					visibleItemCount+ ",totalItemCount="+ totalItemCount+ ")");
			
			final int loadtotal = totalItemCount;
			int lastItemid = listView.getLastVisiblePosition();//��ȡ��ǰ��Ļ���Item��ID
			if((lastItemid+1) == totalItemCount){//�ﵽ���ݵ����һ����¼
				if(totalItemCount > 0){
					//��ǰҳ
					int currentpage = totalItemCount%number == 0 ? totalItemCount/number : totalItemCount/number+1;
					int nextpage = currentpage + 1;//��һҳ
					if(nextpage <= maxpage && loadfinish){
						loadfinish = false;
						listView.addFooterView(footer);
						
						new Thread(new Runnable() {						
							public void run() {
								try {
									Thread.sleep(3000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								List<String> result = DataService.getData(loadtotal, number);
								handler.sendMessage(handler.obtainMessage(100, result));
							}
						}).start();
					}		
				}
						
			}
		}
    }
    
    Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			data.addAll((List<String>) msg.obj);
			adapter.notifyDataSetChanged();//����ListView�����Ѿ������ı䣬Ҫ��ListView���½�����ʾ
			if(listView.getFooterViewsCount() > 0) listView.removeFooterView(footer);
			loadfinish = true;
		}    	
    };
    
}