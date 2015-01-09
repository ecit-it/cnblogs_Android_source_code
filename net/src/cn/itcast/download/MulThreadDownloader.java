package cn.itcast.download;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class MulThreadDownloader {

	public static void main(String[] args) throws Exception {
		String path = "http://192.168.1.100:8080/web/QQWubiSetup.exe";
		int threadsize = 3;
		new MulThreadDownloader().download(path, threadsize);

	}

	private void download(String path, int threadsize) throws Exception {
		URL downpath = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) downpath.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		if(conn.getResponseCode() == 200){
			int length = conn.getContentLength();//��ȡ�����ļ��ĳ���
			File file = new File(getFileName(path));
			RandomAccessFile accessFile = new RandomAccessFile(file, "rwd");//���ɱ����ļ�
			accessFile.setLength(length);
			accessFile.close();
			//����ÿ���̸߳������ص�������
			int block = length % threadsize == 0 ? length / threadsize : length / threadsize +1;
			for(int threadid = 0 ; threadid < threadsize ; threadid++){
				new DownloadThread(threadid, downpath, block, file).start();
			}
		}
	}
	//�������ز���
	private final class DownloadThread extends Thread{
		private int threadid;
		private URL downpath;
		private int block;
		private File file;
		
		public DownloadThread(int threadid, URL downpath, int block, File file) {
			this.threadid = threadid;
			this.downpath = downpath;
			this.block = block;
			this.file = file;
		}
		public void run() {
			int startposition = threadid * block;//�������ļ���ʲôλ�ÿ�ʼ��������
			int endposition = (threadid+1) * block - 1;//���ص������ļ���ʲôλ�ý���
			//ָʾ���߳�Ҫ�������ļ���startpositionλ�ÿ�ʼ���أ����ص�endpositionλ�ý���
			//Range:bytes=startposition-endposition
			try{
				RandomAccessFile accessFile = new RandomAccessFile(file, "rwd");
				accessFile.seek(startposition);//�ƶ�ָ�뵽�ļ���ĳ��λ��
				HttpURLConnection conn = (HttpURLConnection) downpath.openConnection();
				conn.setConnectTimeout(5000);
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Range", "bytes="+ startposition+ "-"+ endposition);
				InputStream inStream = conn.getInputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while( (len = inStream.read(buffer)) != -1 ){
					accessFile.write(buffer, 0, len);
				}
				accessFile.close();
				inStream.close();
				System.out.println("��"+ (threadid+1)+ "�߳��������");
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * ��ȡ�ļ�����
	 * @param path ����·��
	 * @return
	 */
	private static String getFileName(String path) {
		return path.substring(path.lastIndexOf("/")+ 1);
	}

}
