package cn.itcast.news;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.itcast.domain.News;
import cn.itcast.service.VideoNewsService;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        ListView listView = (ListView) this.findViewById(R.id.listView);
        try {
			List<News> videos = VideoNewsService.getLastNews();//需修改成你本机的Http请求路径
			List<HashMap<String, Object>> data = new ArrayList<HashMap<String,Object>>();
			for(News news : videos){
				HashMap<String, Object> item = new HashMap<String, Object>();
				item.put("id", news.getId());
				item.put("title", news.getTitle());
				item.put("timelength", getResources().getString(R.string.timelength)
						+ news.getTimelength()+ getResources().getString(R.string.min));
				data.add(item);
			}
			SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.item,
					new String[]{"title", "timelength"}, new int[]{R.id.title, R.id.timelength});
			listView.setAdapter(adapter);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}