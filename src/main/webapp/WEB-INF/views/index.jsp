<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Spring Mini-Project</title>
</head>
<body>
<jsp:include page="header.jsp"></jsp:include>
	<div class="container">
		<h1>index.jsp</h1>
		<div>${loginUser }</div>
	</div>
<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>
