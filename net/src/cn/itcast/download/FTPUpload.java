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
 * 采用FTP上传文件
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
			socket = new Socket(host, port);//建立指令通道 
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			String line = reader.readLine();//连接上FTP服务器后得到返回的数据，如：220 Serv-U FTP Server v6.3 for WinSock ready.
			System.out.println("step1 ----- " + line);
			
			sendCommand("USER " + user);// 输入登录用户名
			line = reader.readLine();//服务器返回如：331 User name okay, need password
			System.out.println("step2 -----" + line);
			
			sendCommand("PASS " + pass);//  输入登录密码
			line = reader.readLine();//服务器返回如：230 User logged in, proceed
			System.out.println("step3 -----" + line);
		} catch (UnknownHostException ex) {
			System.out.println("Couldn't find the Ftp Server");
		} catch (IOException ex) {
			System.out.println("IOException");
		}
	}
	/**
	 * 退出登录
	 */
	public synchronized void disconnect() throws IOException {

		try {
			sendCommand("QUIT");
			//服务器返回：221 Goodbye!
			System.out.println("last step ----- " + reader.readLine());
		} finally {
			socket = null;
		}
	}
	/**
	 * 获取指定路径下的文件列表
	 * @param serverPath
	 * @throws IOException
	 */
	public synchronized void listFiles(String serverPath) throws IOException {
		writer.write("cwd  " + serverPath + "\r\n"); //改变目录
		writer.flush();
		System.out.println(reader.readLine());

		sendCommand("PASV");//发送PASV命令要求服务器把服务器监听数据连接的IP和端口发送给客户端，由客户端连接到服务器，发送数据

		String response = reader.readLine();//返回格式：227 Entering Passive Mode (220,100,23,11,30,4)
		String ip = null;
		int port1 = -1;
		int opening = response.indexOf('(');
		int closing = response.indexOf(')', opening + 1);

		if (closing > 0) {
			//解析出来后的数据格式为：220,100,23,11,30,4，前面四个数据为IP,后面两个数字用于得到端口号.端口=30*256+4=7684
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

		writer.write("LIST  " + "\r\n");//如果是文件名列出文件信息，如果是目录则列出文件列表
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
		String response = reader.readLine();//返回格式：227 Entering Passive Mode (220,100,23,11,30,4)
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
		Socket dataSocket = new Socket(ip, port);//建立数据通道 
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
FTP协议
相比其他协议，如 HTTP 协议，FTP 协议要复杂一些。与一般的 C/S 应用不同点在于一般的C/S 应用程序一般只会建立一个 Socket 连接，
这个连接同时处理服务器端和客户端的连接命令和数据传输。而FTP协议中将命令与数据分开传送的方法提高了效率。
FTP 使用 2 个端口，一个数据端口和一个命令端口（也叫做控制端口）。这两个端口一般是21 （命令端口）和 20 （数据端口）。
控制 Socket 用来传送命令，数据 Socket 是用于传送数据。每一个 FTP 命令发送之后，FTP 服务器都会返回一个字符串，
其中包括一个响应代码和一些说明信息。其中的返回码主要是用于判断命令是否被成功执行了。

命令端口
一般来说，客户端有一个 Socket 用来连接 FTP 服务器的相关端口，它负责 FTP 命令的发送和接收返回的响应信息。
一些操作如“登录”、“改变目录”、“删除文件”，依靠这个连接发送命令就可完成。

数据端口
对于有数据传输的操作，主要是显示目录列表，上传、下载文件，我们需要依靠另一个 Socket来完成。
如果使用被动模式，通常服务器端会返回一个端口号。客户端需要用另开一个 Socket 来连接这个端口，
然后我们可根据操作来发送命令，数据会通过新开的一个端口传输。
如果使用主动模式，通常客户端会发送一个端口号给服务器端，并在这个端口监听。服务器需要连接到客户端开启的这个数据端口，
并进行数据的传输。

下面对 FTP 的主动模式和被动模式做一个简单的介绍。
主动模式 (PORT)
主动模式下，客户端随机打开一个大于 1024 的端口向服务器的命令端口 P，即 21 端口，发起连接，同时开放N +1 端口监听，
并向服务器发出 “port N+1” 命令，由服务器从它自己的数据端口 (20) 主动连接到客户端指定的数据端口 (N+1)。
FTP 的客户端只是告诉服务器自己的端口号，让服务器来连接客户端指定的端口。对于客户端的防火墙来说，
这是从外部到内部的连接，可能会被阻塞。

被动模式 (PASV)
为了解决服务器发起到客户的连接问题，有了另一种 FTP 连接方式，即被动方式。命令连接和数据连接都由客户端发起，
这样就解决了从服务器到客户端的数据端口的连接被防火墙过滤的问题。
被动模式下，当开启一个 FTP 连接时，客户端打开两个任意的本地端口 (N > 1024 和 N+1) 。
第一个端口连接服务器的 21 端口，提交 PASV 命令。然后，服务器会开启一个任意的端口 (P > 1024 )，
返回如“227 entering passive mode (127,0,0,1,4,18)”。 它返回了 227 开头的信息，在括号中有以逗号隔开的六个数字，
前四个指服务器的地址，最后两个，将倒数第二个乘 256 再加上最后一个数字，这就是 FTP 服务器开放的用来进行数据传输的端口。
如得到 227 entering passive mode (h1,h2,h3,h4,p1,p2)，那么端口号是 p1*256+p2，ip 地址为h1.h2.h3.h4。
这意味着在服务器上有一个端口被开放。客户端收到命令取得端口号之后, 会通过 N+1 号端口连接服务器的端口 P，
然后在两个端口之间进行数据传输。

主要用到的 FTP 命令
FTP 每个命令都有 3 到 4 个字母组成，命令后面跟参数，用空格分开。每个命令都以 "\r\n"结束。
要下载或上传一个文件，首先要登入 FTP 服务器，然后发送命令，最后退出。
这个过程中，主要用到的命令有 USER、PASS、SIZE、REST、CWD、RETR、PASV、PORT、QUIT。
USER: 指定用户名。通常是控制连接后第一个发出的命令。“USER gaoleyi\r\n”： 用户名为gaoleyi 登录。
PASS: 指定用户密码。该命令紧跟 USER 命令后。“PASS gaoleyi\r\n”：密码为 gaoleyi。
SIZE: 从服务器上返回指定文件的大小。“SIZE file.txt\r\n”：如果 file.txt 文件存在，则返回该文件的大小。
CWD: 改变工作目录。如：“CWD dirname\r\n”。
PASV: 让服务器在数据端口监听，进入被动模式。如：“PASV\r\n”。
PORT: 告诉 FTP 服务器客户端监听的端口号，让 FTP 服务器采用主动模式连接客户端。如：“PORT h1,h2,h3,h4,p1,p2”。
RETR: 下载文件。“RETR file.txt \r\n”：下载文件 file.txt。
STOR: 上传文件。“STOR file.txt\r\n”：上传文件 file.txt。
REST: 该命令并不传送文件，而是略过指定点后的数据。此命令后应该跟其它要求文件传输的 FTP 命令。“REST 100\r\n”：重新指定文件传送的偏移量为 100 字节。
QUIT: 关闭与服务器的连接。

FTP 响应码
客户端发送 FTP 命令后，服务器返回响应码。
响应码用三位数字编码表示：
第一个数字给出了命令状态的一般性指示，比如响应成功、失败或不完整。
第二个数字是响应类型的分类，如 2 代表跟连接有关的响应，3 代表用户认证。
第三个数字提供了更加详细的信息。
第一个数字的含义如下：
1 表示服务器正确接收信息，还未处理。
2 表示服务器已经正确处理信息。
3 表示服务器正确接收信息，正在处理。
4 表示信息暂时错误。
5 表示信息永久错误。
第二个数字的含义如下：
0 表示语法。
1 表示系统状态和信息。
2 表示连接状态。
3 表示与用户认证有关的信息。
4 表示未定义。
5 表示与文件系统有关的信息。

指令大全：
ACCT <account> 系统特权帐号 
ALLO <bytes> 为服务器上的文件存储器分配字节 
APPE <filename> 添加文件到服务器同名文件 
CDUP <dir path> 改变服务器上的父目录 
CWD <dir path> 改变服务器上的工作目录 
DELE <filename> 删除服务器上的指定文件 
HELP <command> 返回指定命令信息 
LIST <name> 如果是文件名列出文件信息，如果是目录则列出文件列表 
MODE <mode> 传输模式（S=流模式，B=块模式，C=压缩模式） 
MKD <directory> 在服务器上建立指定目录 
NLST <directory> 列出指定目录内容 
NOOP 无动作，除了来自服务器上的承认 
PASS <password> 系统登录密码 
PASV 请求服务器等待数据连接 
PORT <address> IP 地址和两字节的端口 ID 
PWD 显示当前工作目录 
QUIT 从 FTP 服务器上退出登录 
REIN 重新初始化登录状态连接 
REST <offset> 由特定偏移量重启文件传递 
RETR <filename> 从服务器上找回（复制）文件 
RMD <directory> 在服务器上删除指定目录 
RNFR <old path> 对旧路径重命名 
RNTO <new path> 对新路径重命名 
SITE <params> 由服务器提供的站点特殊参数 
SMNT <pathname> 挂载指定文件结构 
STAT <directory> 在当前程序或目录上返回信息 
STOR <filename> 储存（复制）文件到服务器上 
STOU <filename> 储存文件到服务器名称上 
STRU <type> 数据结构（F=文件，R=记录，P=页面） 
SYST 返回服务器使用的操作系统 
TYPE <data type> 数据类型（A=ASCII，E=EBCDIC，I=binary） 
USER <username>> 系统登录的用户名


断点上传原理：
1、获取服务器上文件的(LIST或SIZE)
2、向服务器发送“APPE ＋文件名”，指定需要断点续传的文件，接下来从数据通道发送的数据会附加到这个文件末尾。
3、使用RandomAccessFile.seek()指定从文件某个位置开始读取数据并发送。
交互指令如下：
Client连接到FTP服务器后

Server返回:220 Serv-U FTP Server v6.3 for WinSock ready.

Client发送:USER liming
Server返回:331 User name okay, need password.

Client发送:PASS 123456
Server返回:230 User logged in, proceed.

Client发送:SIZE xxx.TXT
Server返回:213 5

Client发送:PASV
Server返回:227 Entering Passive Mode (127,0,0,1,11,79)

Client发送:APPE xxx.TXT
Server返回:150 Opening ASCII mode data connection for 1.TXT.

客户端建立另一个Socket连接到127,0,0,1的11*256+79端口，然后使用RandomAccessFile.seek()指定从文件某个位置开始读取数据并发送
Server返回:226 Transfer complete.

Client发送:QUIT
Server返回:221 Goodbye!
*/