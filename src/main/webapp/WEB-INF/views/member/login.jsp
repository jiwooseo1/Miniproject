<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<title>로그인</title>
<script type="text/javascript">
	$(function() {
		
	// addAttribute를 사용한 경우 (쿼리스트링이 붙음)
// 		let loginStatus = '${param.status}';
// 		if (loginStatus == 'fail'){
// 			alert("아이디와 패스워드를 다시 확인해주세요");
// 		}


	// addFlashAttribute를 사용한 경우(쿼리스트링이 안붙음)
		let loginStatus = '${status}';
		if (loginStatus == 'fail'){
			alert("아이디와 패스워드를 다시 확인해주세요");
		}
	})


</script>
</head>
<body>
	<jsp:include page="../header.jsp"></jsp:include>
	<div class="container">
		<h1>로 그 인</h1>
		
		<form action="login" method="post">
			<div class="mb-3 mt-3">
				<label for="userId" class="form-label">아이디:</label> 
				<input type="text" class="form-control" id="userId" placeholder="Enter your id" name="userId">
			</div>

			<div class="mb-3 mt-3">
				<label for="userPwd" class="form-label">비밀번호:</label> 
				<input type="password" class="form-control" id="userPwd" placeholder="Enter your password" name="userPwd">
			</div>
			
			<div class="mb-3 mt-3">
				 <input type="checkbox" class="form-check-input" id="remember" name="remember" >
      			 <label class="form-check-label" for="remember">자동로그인</label>
			</div>
			
			<button type="submit" class="btn btn-success" >로그인</button>
			<button type="reset" class="btn btn-danger">취소</button>
		</form>
		
		
	</div>
	<jsp:include page="../footer.jsp"></jsp:include>
</body>
</html>