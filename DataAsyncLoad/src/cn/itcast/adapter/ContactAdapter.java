package cn.itcast.adapter;

import java.io.File;
import java.util.List;

import cn.itcast.asyncload.R;
import cn.itcast.domain.Contact;
import cn.itcast.service.ContactService;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactAdapter extends BaseAdapter {
	private List<Contact> data;
	private int listviewItem;
	private File cache;
	LayoutInflater layoutInflater;
	
	public ContactAdapter(Context context, List<Contact> data, int listviewItem, File cache) {
		this.data = data;
		this.listviewItem = listviewItem;
		this.cache = cache;
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	/**
	 * 得到数据的总数
	 */
	public int getCount() {
		return data.size();
	}
	/**
	 * 根据数据索引得到集合所对应的数据
	 */
	public Object getItem(int position) {
		return data.get(position);
	}
	
	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView = null;
		TextView textView = null;
		
		if(convertView == null){
			convertView = layoutInflater.inflate(listviewItem, null);
			imageView = (ImageView) convertView.findViewById(R.id.imageView);
			textView = (TextView) convertView.findViewById(R.id.textView);
			convertView.setTag(new DataWrapper(imageView, textView));
		}else{
			DataWrapper dataWrapper = (DataWrapper) convertView.getTag();
			imageView = dataWrapper.imageView;
			textView = dataWrapper.textView;	
		}
		Contact contact = data.get(position);
		textView.setText(contact.name);
		asyncImageLoad(imageView, contact.image);
		return convertView;
	}
    private void asyncImageLoad(ImageView imageView, String path) {
    	AsyncImageTask asyncImageTask = new AsyncImageTask(imageView);
    	asyncImageTask.execute(path);
		
	}
    
    private final class AsyncImageTask extends AsyncTask<String, Integer, Uri>{
    	private ImageView imageView;
		public AsyncImageTask(ImageView imageView) {
			this.imageView = imageView;
		}
		protected Uri doInBackground(String... params) {//子线程中执行的
			try {
				return ContactService.getImage(params[0], cache);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		protected void onPostExecute(Uri result) {//运行在主线程
			if(result!=null && imageView!= null)
				imageView.setImageURI(result);
		}	
    }
	/*
	private void asyncImageLoad(final ImageView imageView, final String path) {
		final Handler handler = new Handler(){
			public void handleMessage(Message msg) {//运行在主线程中
				Uri uri = (Uri)msg.obj;
				if(uri!=null && imageView!= null)
					imageView.setImageURI(uri);
			}
		};
		
		Runnable runnable = new Runnable() {			
			public void run() {
				try {
					Uri uri = ContactService.getImage(path, cache);
					handler.sendMessage(handler.obtainMessage(10, uri));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		new Thread(runnable).start();
	}
*/
	private final class DataWrapper{
		public ImageView imageView;
		public TextView textView;
		public DataWrapper(ImageView imageView, TextView textView) {
			this.imageView = imageView;
			this.textView = textView;
		}
	}
}
