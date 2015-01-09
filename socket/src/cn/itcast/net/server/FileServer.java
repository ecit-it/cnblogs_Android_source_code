package cn.itcast.net.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.itcast.utils.StreamTool;

public class FileServer {
	
	 private ExecutorService executorService;//�̳߳�
	 private int port;//�����˿�
	 private boolean quit = false;//�˳�
	 private ServerSocket server;
	 private Map<Long, FileLog> datas = new HashMap<Long, FileLog>();//��Ŷϵ�����
	 
	 public FileServer(int port){
		 this.port = port;
		 //�����̳߳أ����о���(cpu����*50)���߳�
		 executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 50);
	 }
	 /**
	  * �˳�
	  */
	 public void quit(){
		this.quit = true;
		try {
			server.close();
		} catch (IOException e) {
		}
	 }
	 /**
	  * ��������
	  * @throws Exception
	  */
	 public void start() throws Exception{
		 server = new ServerSocket(port);
		 while(!quit){
	         try {
	           Socket socket = server.accept();
	           //Ϊ֧�ֶ��û��������ʣ������̳߳ع���ÿһ���û�����������
	           executorService.execute(new SocketTask(socket));
	         } catch (Exception e) {
	           //  e.printStackTrace();
	         }
	     }
	 }
	 
	 private final class SocketTask implements Runnable{
		private Socket socket = null;
		public SocketTask(Socket socket) {
			this.socket = socket;
		}
		
		public void run() {
			try {
				System.out.println("accepted connection "+ socket.getInetAddress()+ ":"+ socket.getPort());
				PushbackInputStream inStream = new PushbackInputStream(socket.getInputStream());
				//�õ��ͻ��˷����ĵ�һ��Э�����ݣ�Content-Length=143253434;filename=xxx.3gp;sourceid=
				//����û������ϴ��ļ���sourceid��ֵΪ�ա�
				String head = StreamTool.readLine(inStream);
				System.out.println(head);
				if(head!=null){
					//�����Э����������ȡ�������ֵ
					String[] items = head.split(";");
					String filelength = items[0].substring(items[0].indexOf("=")+1);
					String filename = items[1].substring(items[1].indexOf("=")+1);
					String sourceid = items[2].substring(items[2].indexOf("=")+1);		
					long id = System.currentTimeMillis();//������Դid�������ҪΨһ�ԣ����Բ���UUID
					FileLog log = null;
					if(sourceid!=null && !"".equals(sourceid)){
						id = Long.valueOf(sourceid);
						log = find(id);//�����ϴ����ļ��Ƿ�����ϴ���¼
					}
					File file = null;
					int position = 0;
					if(log==null){//����������ϴ���¼,Ϊ�ļ���Ӹ��ټ�¼
						String path = new SimpleDateFormat("yyyy/MM/dd/HH/mm").format(new Date());
						File dir = new File("file/"+ path);
						if(!dir.exists()) dir.mkdirs();
						file = new File(dir, filename);
						if(file.exists()){//����ϴ����ļ�����������Ȼ����и���
							filename = filename.substring(0, filename.indexOf(".")-1)+ dir.listFiles().length+ filename.substring(filename.indexOf("."));
							file = new File(dir, filename);
						}
						save(id, file);
					}else{// ��������ϴ���¼,��ȡ�Ѿ��ϴ������ݳ���
						file = new File(log.getPath());//���ϴ���¼�еõ��ļ���·��
						if(file.exists()){
							File logFile = new File(file.getParentFile(), file.getName()+".log");
							if(logFile.exists()){
								Properties properties = new Properties();
								properties.load(new FileInputStream(logFile));
								position = Integer.valueOf(properties.getProperty("length"));//��ȡ�Ѿ��ϴ������ݳ���
							}
						}
					}
					
					OutputStream outStream = socket.getOutputStream();
					String response = "sourceid="+ id+ ";position="+ position+ "\r\n";
					//�������յ��ͻ��˵�������Ϣ�󣬸��ͻ��˷�����Ӧ��Ϣ��sourceid=1274773833264;position=0
					//sourceid�ɷ����������ɣ�Ψһ��ʶ�ϴ����ļ���positionָʾ�ͻ��˴��ļ���ʲôλ�ÿ�ʼ�ϴ�
					outStream.write(response.getBytes());
					
					RandomAccessFile fileOutStream = new RandomAccessFile(file, "rwd");
					if(position==0) fileOutStream.setLength(Integer.valueOf(filelength));//�����ļ�����
					fileOutStream.seek(position);//ָ�����ļ����ض�λ�ÿ�ʼд������
					byte[] buffer = new byte[1024];
					int len = -1;
					int length = position;
					while( (len=inStream.read(buffer)) != -1){//���������ж�ȡ����д�뵽�ļ���
						fileOutStream.write(buffer, 0, len);
						length += len;
						Properties properties = new Properties();
						properties.put("length", String.valueOf(length));
						FileOutputStream logFile = new FileOutputStream(new File(file.getParentFile(), file.getName()+".log"));
						properties.store(logFile, null);//ʵʱ��¼�Ѿ����յ��ļ�����
						logFile.close();
					}
					if(length==fileOutStream.length()) delete(id);
					fileOutStream.close();					
					inStream.close();
					outStream.close();
					file = null;
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
	            try {
	                if(socket!=null && !socket.isClosed()) socket.close();
	            } catch (IOException e) {}
	        }
		}
	 }
	 
	 public FileLog find(Long sourceid){
		 return datas.get(sourceid);
	 }
	 //�����ϴ���¼
	 public void save(Long id, File saveFile){
		 //�պ���Ըĳ�ͨ�����ݿ���
		 datas.put(id, new FileLog(id, saveFile.getAbsolutePath()));
	 }
	 //���ļ��ϴ���ϣ�ɾ����¼
	 public void delete(long sourceid){
		 if(datas.containsKey(sourceid)) datas.remove(sourceid);
	 }
	 
	 private class FileLog{
		private Long id;
		private String path;
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getPath() {
			return path;
		}
		public void setPath(String path) {
			this.path = path;
		}
		public FileLog(Long id, String path) {
			this.id = id;
			this.path = path;
		}	
	 }

}
