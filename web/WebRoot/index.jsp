<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="/web/ManageServlet" method="post" enctype="multipart/form-data">
		视频标题：<input name="title" type="text"><br/>
		视频时长：<input name="timelength" type="text"><br/>
		文件：<input type="file" name="videofile"/>
		<input type="submit" value=" 提 交 "/>
	</form>
</body>
</html>