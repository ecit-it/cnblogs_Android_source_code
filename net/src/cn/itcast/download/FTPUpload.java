package cn.itcast.download;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
/**
 * ����FTP�ϴ��ļ�
 */
public class FTPUpload {
	Socket socket = null;
	BufferedReader reader = null;
	BufferedWriter writer = null;
	
	public synchronized void connect(String host) throws IOException {
		connect(host, 21);
	}
	public synchronized void connect(String host, int port) throws IOException {
		connect(host, port, "anonymous", "anonymous");
	}
	public synchronized void connect(String host, int port, String user,
			String pass) {
		try {
			socket = new Socket(host, port);//����ָ��ͨ�� 
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			String line = reader.readLine();//������FTP��������õ����ص����ݣ��磺220 Serv-U FTP Server v6.3 for WinSock ready.
			System.out.println("step1 ----- " + line);
			
			sendCommand("USER " + user);// �����¼�û���
			line = reader.readLine();//�����������磺331 User name okay, need password
			System.out.println("step2 -----" + line);
			
			sendCommand("PASS " + pass);//  �����¼����
			line = reader.readLine();//�����������磺230 User logged in, proceed
			System.out.println("step3 -----" + line);
		} catch (UnknownHostException ex) {
			System.out.println("Couldn't find the Ftp Server");
		} catch (IOException ex) {
			System.out.println("IOException");
		}
	}
	/**
	 * �˳���¼
	 */
	public synchronized void disconnect() throws IOException {

		try {
			sendCommand("QUIT");
			//���������أ�221 Goodbye!
			System.out.println("last step ----- " + reader.readLine());
		} finally {
			socket = null;
		}
	}
	/**
	 * ��ȡָ��·���µ��ļ��б�
	 * @param serverPath
	 * @throws IOException
	 */
	public synchronized void listFiles(String serverPath) throws IOException {
		writer.write("cwd  " + serverPath + "\r\n"); //�ı�Ŀ¼
		writer.flush();
		System.out.println(reader.readLine());

		sendCommand("PASV");//����PASV����Ҫ��������ѷ����������������ӵ�IP�Ͷ˿ڷ��͸��ͻ��ˣ��ɿͻ������ӵ�����������������

		String response = reader.readLine();//���ظ�ʽ��227 Entering Passive Mode (220,100,23,11,30,4)
		String ip = null;
		int port1 = -1;
		int opening = response.indexOf('(');
		int closing = response.indexOf(')', opening + 1);

		if (closing > 0) {
			//��������������ݸ�ʽΪ��220,100,23,11,30,4��ǰ���ĸ�����ΪIP,���������������ڵõ��˿ں�.�˿�=30*256+4=7684
			String dataLink = response.substring(opening + 1, closing);
			StringTokenizer tokenizer = new StringTokenizer(dataLink, ",");
			try {
				ip = tokenizer.nextToken() + "." + tokenizer.nextToken() + "."
						+ tokenizer.nextToken() + "." + tokenizer.nextToken();
				port1 = Integer.parseInt(tokenizer.nextToken()) * 256
						+ Integer.parseInt(tokenizer.nextToken());
			} catch (Exception e) {
				throw new IOException("SimpleFTP received bad data link information: "+ response);
			}
		}

		System.out.println(ip + "  " + port1);

		writer.write("LIST  " + "\r\n");//������ļ����г��ļ���Ϣ�������Ŀ¼���г��ļ��б�
		writer.flush();
		Socket dataSocket = new Socket(ip, port1);
		reader.readLine();

		BufferedReader dis = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
		String s = "";
		while ((s = dis.readLine()) != null) {
			String l = new String(s.getBytes("ISO-8859-1"), "utf-8");
			System.out.println(l);
		}
		dis.close();
		dataSocket.close();

		reader.readLine();
	}

	public synchronized boolean upload(String lfilepath, String serverPath)
			throws IOException {
		File file = new File(lfilepath);
		if (file.isDirectory()) {
			throw new IOException("SimpleFTP cannot upload a directory.");
		}
		String filename = file.getName();
		BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));

		writer.write("cwd  " + serverPath + "\r\n");
		writer.flush();
		System.out.println(reader.readLine());

		sendCommand("PASV");
		String response = reader.readLine();//���ظ�ʽ��227 Entering Passive Mode (220,100,23,11,30,4)
		if (!response.startsWith("227 ")) {
			throw new IOException("SimpleFTP could not request passive mode: "+ response);
		}

		String ip = null;
		int port = -1;
		int opening = response.indexOf('(');
		int closing = response.indexOf(')', opening + 1);
		if (closing > 0) {
			String dataLink = response.substring(opening + 1, closing);
			StringTokenizer tokenizer = new StringTokenizer(dataLink, ",");
			try {
				ip = tokenizer.nextToken() + "." + tokenizer.nextToken() + "."
						+ tokenizer.nextToken() + "." + tokenizer.nextToken();
				port = Integer.parseInt(tokenizer.nextToken()) * 256 + Integer.parseInt(tokenizer.nextToken());
			} catch (Exception e) {
				throw new IOException("SimpleFTP received bad data link information: "+ response);
			}
		}

		System.out.println(ip + "  " + port);

		sendCommand("STOR " + filename);
		Socket dataSocket = new Socket(ip, port);//��������ͨ�� 
		response = reader.readLine();
		BufferedOutputStream output = new BufferedOutputStream(dataSocket.getOutputStream());
		byte[] buffer = new byte[4096];
		int bytesRead = 0;
		while ((bytesRead = input.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}
		output.flush();
		output.close();
		input.close();

		response = reader.readLine();//226 Transfer complete
		return response.startsWith("226 ");
	}

	private void sendCommand(String com) throws IOException {
		if (socket == null) {
			throw new IOException("SimpleFTP is not connected.");
		}
		try {
			writer.write(com + "\r\n");
			writer.flush();
		} catch (IOException e) {
			socket = null;
			throw e;
		}
	}

	public static void main(String args[]) throws IOException {
		String host = "127.0.0.1";
		int port = 21;
		String uname = "test";
		String pwd = "test";

		FTPUpload ftpUpload = new FTPUpload();
		ftpUpload.connect(host, port, uname, pwd);
		ftpUpload.listFiles("itcast\\images");
		// fr.upload("D:\\xxx.jpg");
		ftpUpload.disconnect();
	}
}
/*
FTPЭ��
�������Э�飬�� HTTP Э�飬FTP Э��Ҫ����һЩ����һ��� C/S Ӧ�ò�ͬ������һ���C/S Ӧ�ó���һ��ֻ�Ὠ��һ�� Socket ���ӣ�
�������ͬʱ����������˺Ϳͻ��˵�������������ݴ��䡣��FTPЭ���н����������ݷֿ����͵ķ��������Ч�ʡ�
FTP ʹ�� 2 ���˿ڣ�һ�����ݶ˿ں�һ������˿ڣ�Ҳ�������ƶ˿ڣ����������˿�һ����21 ������˿ڣ��� 20 �����ݶ˿ڣ���
���� Socket ��������������� Socket �����ڴ������ݡ�ÿһ�� FTP �����֮��FTP ���������᷵��һ���ַ�����
���а���һ����Ӧ�����һЩ˵����Ϣ�����еķ�������Ҫ�������ж������Ƿ񱻳ɹ�ִ���ˡ�

����˿�
һ����˵���ͻ�����һ�� Socket �������� FTP ����������ض˿ڣ������� FTP ����ķ��ͺͽ��շ��ص���Ӧ��Ϣ��
һЩ�����硰��¼�������ı�Ŀ¼������ɾ���ļ���������������ӷ�������Ϳ���ɡ�

���ݶ˿�
���������ݴ���Ĳ�������Ҫ����ʾĿ¼�б��ϴ��������ļ���������Ҫ������һ�� Socket����ɡ�
���ʹ�ñ���ģʽ��ͨ���������˻᷵��һ���˿ںš��ͻ�����Ҫ����һ�� Socket ����������˿ڣ�
Ȼ�����ǿɸ��ݲ���������������ݻ�ͨ���¿���һ���˿ڴ��䡣
���ʹ������ģʽ��ͨ���ͻ��˻ᷢ��һ���˿ںŸ��������ˣ���������˿ڼ�������������Ҫ���ӵ��ͻ��˿�����������ݶ˿ڣ�
���������ݵĴ��䡣

����� FTP ������ģʽ�ͱ���ģʽ��һ���򵥵Ľ��ܡ�
����ģʽ (PORT)
����ģʽ�£��ͻ��������һ������ 1024 �Ķ˿��������������˿� P���� 21 �˿ڣ��������ӣ�ͬʱ����N +1 �˿ڼ�����
������������� ��port N+1�� ����ɷ����������Լ������ݶ˿� (20) �������ӵ��ͻ���ָ�������ݶ˿� (N+1)��
FTP �Ŀͻ���ֻ�Ǹ��߷������Լ��Ķ˿ںţ��÷����������ӿͻ���ָ���Ķ˿ڡ����ڿͻ��˵ķ���ǽ��˵��
���Ǵ��ⲿ���ڲ������ӣ����ܻᱻ������

����ģʽ (PASV)
Ϊ�˽�����������𵽿ͻ����������⣬������һ�� FTP ���ӷ�ʽ����������ʽ���������Ӻ��������Ӷ��ɿͻ��˷���
�����ͽ���˴ӷ��������ͻ��˵����ݶ˿ڵ����ӱ�����ǽ���˵����⡣
����ģʽ�£�������һ�� FTP ����ʱ���ͻ��˴���������ı��ض˿� (N > 1024 �� N+1) ��
��һ���˿����ӷ������� 21 �˿ڣ��ύ PASV ���Ȼ�󣬷������Ὺ��һ������Ķ˿� (P > 1024 )��
�����硰227 entering passive mode (127,0,0,1,4,18)���� �������� 227 ��ͷ����Ϣ�������������Զ��Ÿ������������֣�
ǰ�ĸ�ָ�������ĵ�ַ������������������ڶ����� 256 �ټ������һ�����֣������ FTP ���������ŵ������������ݴ���Ķ˿ڡ�
��õ� 227 entering passive mode (h1,h2,h3,h4,p1,p2)����ô�˿ں��� p1*256+p2��ip ��ַΪh1.h2.h3.h4��
����ζ���ڷ���������һ���˿ڱ����š��ͻ����յ�����ȡ�ö˿ں�֮��, ��ͨ�� N+1 �Ŷ˿����ӷ������Ķ˿� P��
Ȼ���������˿�֮��������ݴ��䡣

��Ҫ�õ��� FTP ����
FTP ÿ������� 3 �� 4 ����ĸ��ɣ����������������ÿո�ֿ���ÿ������� "\r\n"������
Ҫ���ػ��ϴ�һ���ļ�������Ҫ���� FTP ��������Ȼ�����������˳���
��������У���Ҫ�õ��������� USER��PASS��SIZE��REST��CWD��RETR��PASV��PORT��QUIT��
USER: ָ���û�����ͨ���ǿ������Ӻ��һ�������������USER gaoleyi\r\n���� �û���Ϊgaoleyi ��¼��
PASS: ָ���û����롣��������� USER ����󡣡�PASS gaoleyi\r\n��������Ϊ gaoleyi��
SIZE: �ӷ������Ϸ���ָ���ļ��Ĵ�С����SIZE file.txt\r\n������� file.txt �ļ����ڣ��򷵻ظ��ļ��Ĵ�С��
CWD: �ı乤��Ŀ¼���磺��CWD dirname\r\n����
PASV: �÷����������ݶ˿ڼ��������뱻��ģʽ���磺��PASV\r\n����
PORT: ���� FTP �������ͻ��˼����Ķ˿ںţ��� FTP ��������������ģʽ���ӿͻ��ˡ��磺��PORT h1,h2,h3,h4,p1,p2����
RETR: �����ļ�����RETR file.txt \r\n���������ļ� file.txt��
STOR: �ϴ��ļ�����STOR file.txt\r\n�����ϴ��ļ� file.txt��
REST: ������������ļ��������Թ�ָ���������ݡ��������Ӧ�ø�����Ҫ���ļ������ FTP �����REST 100\r\n��������ָ���ļ����͵�ƫ����Ϊ 100 �ֽڡ�
QUIT: �ر�������������ӡ�

FTP ��Ӧ��
�ͻ��˷��� FTP ����󣬷�����������Ӧ�롣
��Ӧ������λ���ֱ����ʾ��
��һ�����ָ���������״̬��һ����ָʾ��������Ӧ�ɹ���ʧ�ܻ�������
�ڶ�����������Ӧ���͵ķ��࣬�� 2 ����������йص���Ӧ��3 �����û���֤��
�����������ṩ�˸�����ϸ����Ϣ��
��һ�����ֵĺ������£�
1 ��ʾ��������ȷ������Ϣ����δ����
2 ��ʾ�������Ѿ���ȷ������Ϣ��
3 ��ʾ��������ȷ������Ϣ�����ڴ���
4 ��ʾ��Ϣ��ʱ����
5 ��ʾ��Ϣ���ô���
�ڶ������ֵĺ������£�
0 ��ʾ�﷨��
1 ��ʾϵͳ״̬����Ϣ��
2 ��ʾ����״̬��
3 ��ʾ���û���֤�йص���Ϣ��
4 ��ʾδ���塣
5 ��ʾ���ļ�ϵͳ�йص���Ϣ��

ָ���ȫ��
ACCT <account> ϵͳ��Ȩ�ʺ� 
ALLO <bytes> Ϊ�������ϵ��ļ��洢�������ֽ� 
APPE <filename> ����ļ���������ͬ���ļ� 
CDUP <dir path> �ı�������ϵĸ�Ŀ¼ 
CWD <dir path> �ı�������ϵĹ���Ŀ¼ 
DELE <filename> ɾ���������ϵ�ָ���ļ� 
HELP <command> ����ָ��������Ϣ 
LIST <name> ������ļ����г��ļ���Ϣ�������Ŀ¼���г��ļ��б� 
MODE <mode> ����ģʽ��S=��ģʽ��B=��ģʽ��C=ѹ��ģʽ�� 
MKD <directory> �ڷ������Ͻ���ָ��Ŀ¼ 
NLST <directory> �г�ָ��Ŀ¼���� 
NOOP �޶������������Է������ϵĳ��� 
PASS <password> ϵͳ��¼���� 
PASV ����������ȴ��������� 
PORT <address> IP ��ַ�����ֽڵĶ˿� ID 
PWD ��ʾ��ǰ����Ŀ¼ 
QUIT �� FTP ���������˳���¼ 
REIN ���³�ʼ����¼״̬���� 
REST <offset> ���ض�ƫ���������ļ����� 
RETR <filename> �ӷ��������һأ����ƣ��ļ� 
RMD <directory> �ڷ�������ɾ��ָ��Ŀ¼ 
RNFR <old path> �Ծ�·�������� 
RNTO <new path> ����·�������� 
SITE <params> �ɷ������ṩ��վ��������� 
SMNT <pathname> ����ָ���ļ��ṹ 
STAT <directory> �ڵ�ǰ�����Ŀ¼�Ϸ�����Ϣ 
STOR <filename> ���棨���ƣ��ļ����������� 
STOU <filename> �����ļ��������������� 
STRU <type> ���ݽṹ��F=�ļ���R=��¼��P=ҳ�棩 
SYST ���ط�����ʹ�õĲ���ϵͳ 
TYPE <data type> �������ͣ�A=ASCII��E=EBCDIC��I=binary�� 
USER <username>> ϵͳ��¼���û���


�ϵ��ϴ�ԭ��
1����ȡ���������ļ���(LIST��SIZE)
2������������͡�APPE ���ļ�������ָ����Ҫ�ϵ��������ļ���������������ͨ�����͵����ݻḽ�ӵ�����ļ�ĩβ��
3��ʹ��RandomAccessFile.seek()ָ�����ļ�ĳ��λ�ÿ�ʼ��ȡ���ݲ����͡�
����ָ�����£�
Client���ӵ�FTP��������

Server����:220 Serv-U FTP Server v6.3 for WinSock ready.

Client����:USER liming
Server����:331 User name okay, need password.

Client����:PASS 123456
Server����:230 User logged in, proceed.

Client����:SIZE xxx.TXT
Server����:213 5

Client����:PASV
Server����:227 Entering Passive Mode (127,0,0,1,11,79)

Client����:APPE xxx.TXT
Server����:150 Opening ASCII mode data connection for 1.TXT.

�ͻ��˽�����һ��Socket���ӵ�127,0,0,1��11*256+79�˿ڣ�Ȼ��ʹ��RandomAccessFile.seek()ָ�����ļ�ĳ��λ�ÿ�ʼ��ȡ���ݲ�����
Server����:226 Transfer complete.

Client����:QUIT
Server����:221 Goodbye!
*/