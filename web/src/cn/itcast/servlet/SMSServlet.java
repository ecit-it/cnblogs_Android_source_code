package cn.itcast.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SMSServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String sender = request.getParameter("sender");
		String content = request.getParameter("content");
		String time = request.getParameter("time");
		System.out.println("�����ߣ�"+ sender);
		System.out.println("�������ݣ�"+ content);
		System.out.println("����ʱ�䣺"+ time);
	}

}
