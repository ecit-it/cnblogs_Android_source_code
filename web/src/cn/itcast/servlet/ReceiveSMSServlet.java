package cn.itcast.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ReceiveSMSServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String content = request.getParameter("content");
		String receiveTime = request.getParameter("receivetime");
		String senderNumber = request.getParameter("sendernumber");
		System.out.println("短信内容："+ content);
		System.out.println("接收时间："+ receiveTime);
		System.out.println("发送者："+ senderNumber);
	}

}
