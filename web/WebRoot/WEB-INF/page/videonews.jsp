<%@ page language="java" contentType="text/xml; charset=UTF-8" pageEncoding="UTF-8"%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><?xml version="1.0" encoding="UTF-8"?>
<videonews><c:forEach items="${videos}" var="video">
	<news id="${video.id}">
		<title>${video.title}</title>
		<timelength>${video.timelength}</timelength>
	</news></c:forEach>
</videonews>