package cn.itcast.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.domain.News;
import cn.itcast.service.VideoNewsService;
import cn.itcast.service.impl.VideoNewsServiceBean;


public class ListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private VideoNewsService service = new VideoNewsServiceBean();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<News> videos = service.getLastNews();
		String format = request.getParameter("format");
		if("json".equals(format)){
			// [{id:56,title:"xxxxx",timelength:90},{id:16,title:"xbbx",timelength:20}]
			StringBuilder builder = new StringBuilder();
			builder.append('[');
			for(News news : videos){
				builder.append('{');
				builder.append("id:").append(news.getId()).append(',');
				builder.append("title:\"").append(news.getTitle()).append("\",");
				builder.append("timelength:").append(news.getTimelength());
				builder.append("},");
			}
			builder.deleteCharAt(builder.length() - 1);
			builder.append(']');
			request.setAttribute("json", builder.toString());
			request.getRequestDispatcher("/WEB-INF/page/jsonvideonews.jsp").forward(request, response);
		}else{
			request.setAttribute("videos", videos);
			request.getRequestDispatcher("/WEB-INF/page/videonews.jsp").forward(request, response);
		}
	}

}
