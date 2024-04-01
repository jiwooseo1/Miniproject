<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<title>게시판 글쓰기</title>
<script type="text/javascript">
	
	$(function(){
		
	$(".upFileArea").on("dragenter dragover", function(evt) {
		evt.preventDefault(); // 진행중인 이벤트 버블링 캔슬	
		});
	
		$(".upFileArea").on("drop", function(evt) {
			evt.preventDefault(); // 진행중인 이벤트 버블링 캔슬
			
			console.log(evt.originalEvent.dataTransfer.files);
			
			let files = evt.originalEvent.dataTransfer.files;
			
			for (let file of files){
				console.log(file);
				
				let form = new FormData(); // 파일을 보낼거니까 formData를 써야함
				form.append("uploadFile", file); // 전송할 데이터 추가
				// 위문장에서 "uploadFile" 파일의 이름은 컨트롤러단의 MultipartFile 매개변수명과 일치시킨다.
				
				$.ajax({
					url : "uploadFile", // 데이터가 송수신될 서버의 주소 (서블릿의 매핑주소 작성)
					type : "POST", // 통신 방식 (GET, POST, PUT, DELETE)
					data : form , // 데이터 보내기
					dataType : "json", // 수신받을 데이터 타입 (MINE TYPE)
					processData : false, // text데이터에 대해서 쿼리스트링 처리를 하지 않겠다.
					contentType : false, // application/x-www-form-urlencoded 처리하지 않음.
					//async : false, // 동기 통신 방식으로 하곘다. (default : true 비동기)
					success : function(data) {
						// 통신이 성공하면 수행할   함수
							console.log(data);
							if (data != null) {
								displayUploadedFile(data);
							}
						
					},
					error : function() {},
					complete : function() {},
				});
				
			}
					
		});
		
	});
	
	function displayUploadedFile(json) {
		let output = "";
		
		$.each(json, function(i, elt) {
			let name = elt.newFileName.replaceAll("\\", "/");
			
			if (elt.thumbFileName != null) { // 이미지
				let thumb = elt.thumbFileName.replaceAll("\\", "/");
				output += `<img src='../resources/uploads\${thumb}' id='\${elt.originalFileName}'/>`;
				output += `<img src='../resources/images/remove.png' class='remIcon' onclick='remFile(this)'/>`;
			} else { // 이미지가 아닌 경우
				output += `<a href='../resources/uploads\${name}'>\${elt.originalFileName}</a>`;
				output += `<img src='../resources/images/remove.png' class='remIcon'/>`;
			}
			
		});
		
		$(".upLoadFiles").html(output);
		
	}
	
// 	function remFile(fileId) {
// 		console.log(fileId);
// 		let removeFile = $(fileId).prev().attr('id'); // 삭제할 파일의 originalName
// 		console.log(removeFile)
		
// 		$.ajax({
// 			url : "remFile", // 데이터가 송수신될 서버의 주소 (서블릿의 매핑주소 작성)
// 			type : "GET", // 통신 방식 (GET, POST, PUT, DELETE)
// 			data : {
// 				"removeFile" : removeFile
// 			} , // 데이터 보내기
// 			dataType : "text", // 수신받을 데이터 타입 (MINE TYPE)
// 			success : function(data) {
// 				// 통신이 성공하면 수행할   함수
// 					console.log(data);
// 				 if (data == 'success'){
// 					 $(fileId).prev().remove();
// 					 $(fileId).remove();
// 				 }	
				
// 			},
// 			error : function() {},
// 			complete : function() {},
// 		});
		
		
// 	}
	
// 	function btnCancel(fileId) {
// 		// 취소버튼 클릭시 드래그드랍한 모든 파일 삭제
		
// 		$.ajax({
// 			url : "remAllFile", // 데이터가 송수신될 서버의 주소 (서블릿의 매핑주소 작성)
// 			type : "GET", // 통신 방식 (GET, POST, PUT, DELETE)
// 			dataType : "text", // 수신받을 데이터 타입 (MINE TYPE)
// 			success : function(data) {
// 				// 통신이 성공하면 수행할   함수
// 				console.log(data);
// 				location.href = "listAll";
				
// 			},
// 			error : function() {},
// 			complete : function() {},
// 		});
// 	}
	
	
</script>
<style type="text/css">
	.upFileArea {
		width : 100%;
		height : 100px;
		border : 1px dotted #333;
		
		font-weight : bold;
		color : green; 
		background-color : #eff9f7;
		
		text-align: center;
		line-height: 100px;
	}
	.remIcon {
		width: 30px;
	}
	
</style>
</head>
<body>
	<jsp:include page="../header.jsp"></jsp:include>

	<div class="container">
	<h1>게시판 글쓰기</h1>
		<form action="writeBoard" method="post">
			<div class="mb-3 mt-3">
				<label for="writer" class="form-label">작성자:</label> 
				<input type="text" class="form-control" id="writer" name="writer"
				value="${sessionScope.loginUser.userId }" readonly>
			</div>

			<div class="mb-3 mt-3">
				<label for="title" class="form-label">제목:</label> 
				<input type="text" class="form-control" id="title" name="title">
			</div>

			<div class="mb-3 mt-3">
				<label for="content" class="form-label">내용:</label>
				<textarea rows="20" style="width: 100%" id="content" name="content"></textarea> 
				
			</div>

			<div class="mb-3 mt-3">
				<label for="upFile" class="form-label">첨부이미지:</label> 
				<div class="upFileArea">업로드할 파일을 드래그앤드랍해 보세요</div>
				<div class="upLoadFiles"></div>
			</div>
			
			<button type="submit" class="btn btn-success" >저장</button>
			<button type="button" class="btn btn-danger" onclick="btnCancel()">취소</button>
			
			<input type="hidden" name="csrfToken" value="${sessionScope.csrfToken }" />
		</form>
	</div>
	
	<jsp:include page="../footer.jsp"></jsp:include>
</body>
</html>