package cn.itcast.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.content.Context;
import android.os.Environment;

public class FileService {
	private Context context;
	
	public FileService(Context context) {
		this.context = context;
	}

	public void saveToSDCard(String filename, String content)throws Exception {
		File file = new File(Environment.getExternalStorageDirectory(), filename);
		FileOutputStream outStream = new FileOutputStream(file);
		outStream.write(content.getBytes());
		outStream.close();
	}
	/**
	 * 保存文件
	 * @param filename 文件名称
	 * @param content 文件内容
	 */
	public void save(String filename, String content) throws Exception {
		//私有操作模式：创建出来的文件只能被本应用访问，其它应用无法访问该文件，另外采用私有操作模式创建的文件，写入文件中的内容会覆盖原文件的内容
		FileOutputStream outStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
		outStream.write(content.getBytes());
		outStream.close();
	}
	
	/**
	 * 保存文件
	 * @param filename 文件名称
	 * @param content 文件内容
	 */
	public void saveAppend(String filename, String content) throws Exception {//ctrl+shift+y / x
		//私有操作模式：创建出来的文件只能被本应用访问，其它应用无法访问该文件，另外采用私有操作模式创建的文件，写入文件中的内容会覆盖原文件的内容
		FileOutputStream outStream = context.openFileOutput(filename, Context.MODE_APPEND);
		outStream.write(content.getBytes());
		outStream.close();
	}
	
	/**
	 * 保存文件
	 * @param filename 文件名称
	 * @param content 文件内容
	 */
	public void saveReadable(String filename, String content) throws Exception {
		//私有操作模式：创建出来的文件只能被本应用访问，其它应用无法访问该文件，另外采用私有操作模式创建的文件，写入文件中的内容会覆盖原文件的内容
		FileOutputStream outStream = context.openFileOutput(filename, Context.MODE_WORLD_READABLE);
		outStream.write(content.getBytes());
		outStream.close();
	}
	
	/**
	 * 保存文件
	 * @param filename 文件名称
	 * @param content 文件内容
	 */
	public void saveWriteable(String filename, String content) throws Exception {
		//私有操作模式：创建出来的文件只能被本应用访问，其它应用无法访问该文件，另外采用私有操作模式创建的文件，写入文件中的内容会覆盖原文件的内容
		FileOutputStream outStream = context.openFileOutput(filename, Context.MODE_WORLD_WRITEABLE);
		outStream.write(content.getBytes());
		outStream.close();
	}
	
	/**
	 * 保存文件
	 * @param filename 文件名称
	 * @param content 文件内容
	 */
	public void saveRW(String filename, String content) throws Exception {
		//私有操作模式：创建出来的文件只能被本应用访问，其它应用无法访问该文件，另外采用私有操作模式创建的文件，写入文件中的内容会覆盖原文件的内容
		FileOutputStream outStream = context.openFileOutput(filename, Context.MODE_WORLD_WRITEABLE + Context.MODE_WORLD_READABLE);
		outStream.write(content.getBytes());
		outStream.close();
	}
	
	/**
	 * 读取文件内容
	 * @param filename 文件名称
	 * @return 文件内容
	 * @throws Exception
	 */
	public String read(String filename) throws Exception {
		FileInputStream inStream = context.openFileInput(filename);
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while((len = inStream.read(buffer)) != -1){
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		return new String(data);
	}

}
