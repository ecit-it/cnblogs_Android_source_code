package cn.itcast.servlet;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class ManageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String title = request.getParameter("title");		
		String timelength = request.getParameter("timelength");
		System.out.println("视频名称："+ title);
		System.out.println("时长："+ timelength);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if(isMultipart){
			try{
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				upload.setHeaderEncoding("UTF-8");
				List<FileItem> items = upload.parseRequest(request);
				String dir = request.getSession().getServletContext().getRealPath("/files");
				File dirFile = new File(dir);
				if(!dirFile.exists()) dirFile.mkdirs();
				for(FileItem item : items){
					if(item.isFormField()) {//如果文本类型参数
						String name = item.getFieldName();
						String value = item.getString("UTF-8");
						System.out.println(name+ "="+ value);
					}else{//如果文件类型参数
						System.out.println(dir);
						File saveFile = new File(dirFile, item.getName());
						item.write(saveFile);
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			doGet(request, response);
		}
	}

}
