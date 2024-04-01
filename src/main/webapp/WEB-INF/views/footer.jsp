<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<c:set var ="contextPath" value ="<%=request.getContextPath() %>"></c:set>
<style>
	.boFooter{
		background-image: url('${contextPath}/images/iceland.jpg');
		background-size: cover;
	}
</style> 
</head>
<body>
<div class="mt-5 p-4 bg-dark text-white text-center boFooter">
  <p>Footer</p>
</div>
</body>
</html>