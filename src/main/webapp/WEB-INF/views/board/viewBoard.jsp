<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script src="https://kit.fontawesome.com/3dfa264fc9.js" crossorigin="anonymous"></script>
<title>게시글</title>
<script type="text/javascript">
	$(function() {
		$(".closeModal").click(function() {
			$("#delModal").hide();
		});
	});

	function deleteReply() {
		let replyNo = '${newReply.replyNo}';
// 		console.log(${newReply.replyNo});
		
		$.ajax({
			url : "/reply/", // 데이터가 송수신될 서버의 주소 (서블릿의 매핑주소 작성)
			type : "DELETE", // 통신 방식 (GET(읽음), POST(저장), PUT(수정), DELETE(삭제))
			dataType : "json", // 수신받을 데이터 타입 (MINE TYPE)
			success : function(data) {
				console.log(data);
				
			},
			error : function() {
				alert("error 발생");
			},
			complete : function() {
			},
		});
	}
	
	
	

	$(function() {
		
		
// 		let status = '${param.status}';
// 		if (status == 'noPermission') {
// 			alert("수정 권한이 없습니다");
// 		}

// <i class="fa-regular fa-heart" id="dislike"></i>
// <i class="fa-solid fa-heart" id="like"></i>	
		
		
	$(".fa-heart").click(function() {
		alert($(this).attr("id"));
		if ($(this).attr("id") == 'dislike'){ // 안좋아요 -> 좋아요
			$(this).removeClass("fa-regular").addClass("fa-solid");
			$(this).attr("id","like"); // id만 쓰면 가져오는거고 두번째를 쓰면 바꿔준다.
		} else if ($(this).attr("id") == 'like'){ // 좋아요 -> 안좋아요
			$(this).removeClass("fa-solid").addClass("fa-regular");
			$(this).attr("id","dislike");
		}
		
		sendBehavior($(this).attr("id"));
		getAllReplies();
	});	
		
		
		
	});
	
function sendBehavior (behavior) {
	let who = '${sessionScope.loginUser.userId}';
	alert(who);
	if (who == ''){
		location.href="/member/login?redirectUrl=viewBoard&no=${board.no}";
	} else {
		let boardNo = '${board.no}';
		
		
			$.ajax({
				url : "/board/likedislike", // 데이터가 송수신될 서버의 주소 (서블릿의 매핑주소 작성)
				type : "post", // 통신 방식 (GET(읽음), POST(저장), PUT(수정), DELETE(삭제))
				data: {
					"who" : who,
					"boardNo" : boardNo,
					"behavior" : behavior
				},
				dataType : "text", // 수신받을 데이터 타입 (MINE TYPE)
				success : function(data) {
					console.log(data);
				if (data == 'success'){
					location.reload();
				}
			
				},
				error : function() {
					alert("error 발생");
				},
				complete : function() {
				},
			});
		
	}
	
	
	
}
	
	
	
	function getAllReplies() {
		let boardNo = ${board.no};
		let pageNo = 1;
		
		$.ajax({
			url : "/reply/all/" + boardNo + "/" + pageNo, // 데이터가 송수신될 서버의 주소 (서블릿의 매핑주소 작성)
			type : "GET", // 통신 방식 (GET(읽음), POST(저장), PUT(수정), DELETE(삭제))
			dataType : "json", // 수신받을 데이터 타입 (MINE TYPE)
			success : function(data) {
				console.log(data);
				displayAllReplies(data);
// 				$.each(data.replyList, function(i, elt) {
// 					let result = "";
// 					result += i + ' : ' + elt.replyText + ',' + elt.replier
// 					console.log(result);
// 				});
				
			},
			error : function() {
				alert("error 발생");
			},
			complete : function() {
			},
		});
			
	}
	
	//allReplies
	function displayAllReplies(replies) {
		let output = '<ul class="list-group">';
		
		if (replies.length > 0){
			$.each(replies, function(i, elt) {
				output += `<li class="list-group-item">`;
				output += `<div class='replyText'>\${elt.replyText}</div>`;
				output += `<div class='replyInfo'><span>\${elt.replier}</span>`;
				let elapsedTime = procPostDate(elt.postDate)
				output += `<span class='postDate'>\${elapsedTime}</span>`;
				
// 				output += `<span class='postDate'>\${elt.postDate}</span></div>`;	
				
				output += `<div class='btnIcons'><img src='../resources/images/modify.png'/>`;
				output += `<img src='../resources/images/delete.png'/></div>`;
				output += `</li>`;	
			});	
		}
		
		output += '</ul>';
		$(".allReplies").html(output);
	}
	
	
	function procPostDate(date) {
		let postDate = new Date(date); // 댓글 작성일
		let now = new Date(); // 현재 날짜시간
		
		let diff = (now - postDate) / 1000; // 시간 차 (초 단위)
// 		let diff = 3000;
		console.log(diff);
		
		let times = [
			{name: "일", time: 24 * 60 * 60},
			{name : "시간", time : 60 * 60},
			{name : "분", time : 60}
		]
		
		for (let val of times){
			let betweenTime = Math.floor(diff / val.time)
			
			console.log(val.time, diff, betweenTime);
			
			if (betweenTime > 0){
				if (diff > 24 * 60 * 60){ // 1일 이상이 지났다.
					return postDate.toLocaleString();
				}
				
				return betweenTime + val.name + "전";
			}
			
		}
		
		return "방금전";
	}
	
	function saveReply() {
		let parentNo = '${board.no}';
		let replyText = $("#replyText").val();
		let replier = '${sessionScope.loginUser.userId}';
		
		let newReply = {
				"parentNo" : parentNo,
				"replyText" : replyText,
				"replier" : replier
		}
		
		if (replier == ''){ // 로그인하지 않은 유저
			location.href='/member/login?redirectUrl=viewBoard&no='+parentNo;
		} else {
			
		$.ajax({
			url : "/reply/", // 데이터가 송수신될 서버의 주소 (서블릿의 매핑주소 작성)
			type : "POST", // 통신 방식 (GET(읽음), POST(저장), PUT(수정), DELETE(삭제))
			data :  JSON.stringify(newReply), // 보낼 데이터
			headers : {
				// 보내는 데이터의 Mime-type
				"Content-Type" : "application/json",
				
				// PUT, DELETE, PATCH 등의 REST에서 사용되는 http-method가 동작하지 않는
				// 과거의 웹브라우저에게 POST방식으로 동작하도록 한다.
				"X-HTTP-Method-Override" : "POST"
			},
			dataType : "text", // 수신받을 데이터 타입 (MINE TYPE)
			success : function(data) {
				console.log(data);
				if(data == "success"){
					getAllReplies();
					$("#replyText").val("");
				}
			},
			error : function() {
				alert("error 발생");
			},
			complete : function() {
			},
		});
		
	   }
		
	}
	
	
	
</script>
<style>
	.readLikeCnt{
		display: flex;
/* 		justify-content: space-between; */
	}
	.uploadedImage{
		display: flex;
		justify-content: flex-start;
		align-items: center;
		padding: 10px;
	}
	
	.btns{
		margin-top: 20px;
		display: flex;
		justify-content: flex-end;
		align-items: center;
	}
	 
	.btn{
		margin: 5px;
	}
	
	.btnIcons img{
		width : 25px;
	}
	
	.btnIcons {
		float: right;
		margin-right: 5px;
	}
	
	#replyText{
		margin: 20px;
		padding: 10px;
		background-color: #eee;
	}
	
	.replyInfo{
		display: flex;
		justify-content: space-between;
	}
	
	.fa-heart {
		color: red;
	}
	
</style>
</head>
<body>
	<c:if test="${param.status == 'noPermission' }">
		<script type="text/javascript">
			window.alert("No Permission");
		</script>
	</c:if>	
	
	<c:set var="contextPath" value="<%=request.getContextPath()%>"></c:set>
	<jsp:include page="../header.jsp"></jsp:include>

	${requestScope.board } ${requestScope.upLoadFile }

	<div class="container">
		<h1>게시판 글 조회</h1>
		
		<div class="readLikeCnt" >
			<div class="readCount">
				조회수<span class="badge bg-primary">${requestScope.board.readcount }</span>
			</div>
			
			<div class="likeCount">
				좋아요<span class="badge bg-info">${requestScope.board.likecount }</span>	
			
			
<!-- 			<div class="JJim"> -->
<!--                 <button class="likeBtn" ><svg xmlns="http://www.w3.org/2000/svg" height="16" width="16" viewBox="0 0 512 512">!Font Awesome Free 6.5.1 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license/free Copyright 2024 Fonticons, Inc.<path d="M225.8 468.2l-2.5-2.3L48.1 303.2C17.4 274.7 0 234.7 0 192.8v-3.3c0-70.4 50-130.8 119.2-144C158.6 37.9 198.9 47 231 69.6c9 6.4 17.4 13.8 25 22.3c4.2-4.8 8.7-9.2 13.5-13.3c3.7-3.2 7.5-6.2 11.5-9c0 0 0 0 0 0C313.1 47 353.4 37.9 392.8 45.4C462 58.6 512 119.1 512 189.5v3.3c0 41.9-17.4 81.9-48.1 110.4L288.7 465.9l-2.5 2.3c-8.2 7.6-19 11.9-30.2 11.9s-22-4.2-30.2-11.9zM239.1 145c-.4-.3-.7-.7-1-1.1l-17.8-20c0 0-.1-.1-.1-.1c0 0 0 0 0 0c-23.1-25.9-58-37.7-92-31.2C81.6 101.5 48 142.1 48 189.5v3.3c0 28.5 11.9 55.8 32.8 75.2L256 430.7 431.2 268c20.9-19.4 32.8-46.7 32.8-75.2v-3.3c0-47.3-33.6-88-80.1-96.9c-34-6.5-69 5.4-92 31.2c0 0 0 0-.1 .1s0 0-.1 .1l-17.8 20c-.3 .4-.7 .7-1 1.1c-4.5 4.5-10.6 7-16.9 7s-12.4-2.5-16.9-7z"/></svg></button> -->
<!--             </div> -->
		</div>
		
			<div>
<!-- 				<i class="fa-regular fa-heart" id="dislike"></i> -->
<!-- 				<i class="fa-solid fa-heart" id="like"></i> -->

			</div>
<%-- 		<div>${likePeople }</div> --%>


<!--  	이 글을 A님 B님 C님 외 N명이 좋아합니다. -->
		<c:if test="${likePeople.size() le 3}">
			<div>이 글을
			<c:forEach var="who" items="${likePeople }">${who }님</c:forEach>이 좋아합니다.
			</div>
		</c:if>
		<c:if test="${likePeople.size() ge 4}">
			<div>
				<c:forEach var="who" items="${likePeople }" begin="1" end="3">
					${who }님
				</c:forEach>
				외 <span>${likePeople.size() -3 }</span>명이 좋아합니다.
			</div>
		</c:if>
			</div>

		
<!--  	boardNo번 글을 좋아요 한 사람의 경우, solid 하트로 보여주기 -->
	  	<div>
	  		<c:set var="hasHeart" value="false"></c:set>
	  		<c:forEach var="who" items="${likePeople }">
	  			<c:choose>
	  				<c:when test="${loginUser.userId == who }">
	  					<i class="fa-solid fa-heart" id="like"></i>
	  					<c:set var="hasHeart" value="true"></c:set>
	  				</c:when>
	  			</c:choose>
	  			
	  		</c:forEach>
	  		
	  		<c:if test="${hasHeart == false }">
	  			<i class="fa-regular fa-heart" id="dislike"></i>
	  		</c:if>
	  	</div>


		
		
		<div class="mb-3 mt-3">
			<label for="no" class="form-label">글번호:</label> <input type="text"
				class="form-control" id="no" value="${requestScope.board.no }"
				readonly>
		</div>

		<div class="mb-3 mt-3">
			<label for="writer" class="form-label">작성자:</label> <input
				type="text" class="form-control" id="writer"
				value="${requestScope.board.writer }" readonly>
		</div>

		<div class="mb-3 mt-3">
			<label for="title" class="form-label">제목:</label> <input type="text"
				class="form-control" id="title" value="${requestScope.board.title }">

		</div>


		<div class="mb-3 mt-3">
			<label for="content" class="form-label">내용:</label>
			<textarea rows="20" style="width: 100%" id="content">${requestScope.board.content }</textarea>
		<%--<div id ="content">${requestScope.board.content }</div> --%>
			
		</div>
		
		<div>
			<label for="uploadFile" class="form-label">첨부 이미지:</label>
		<c:forEach var = "uf" items = "${upFileList}">
		<c:choose>
			<c:when test="${uf.thumbFileName != null }">
					<div class="uploadedImage">
							<img src="../resources/uploads/${uf.newFileName }" width="200px;" id="uploadFile"/>	
					</div>				
			</c:when>
			<c:otherwise>
					
					<a href="../resources/uploads/${uf.newFileName }" >${uf.newFileName }</a>
					
			</c:otherwise>
		</c:choose>
		</c:forEach>
		
		
		</div>
		
		<c:if test="${requestScope.upLoadFile != null}">
			<div class="mb-3 mt-3">
				<label for="upFile" class="form-label">첨부이미지:</label> <img
					src="${contextPath}/${requestScope.upLoadFile.newFileName }" />
			</div>
			<div>
				<span>${requestScope.upLoadFile.originalFileName }</span>
			</div>
		</c:if>
		
		<!-- 		로그인한 유저가 작성자와 같으면 수정, 삭제버튼을 보여주고 -->
		<!-- 		로그인한 유저가 작성자와 같지않으면 수정, 삭제 disabled 시킨다. -->
	<div class="btns">		
            <button type="button" class="btn btn-success" onclick="location.href='originalEditboard?no=${board.no}';">수정</button>
            <button type="button" class="btn btn-danger" onclick="location.href='remBoard?no=${board.no}';">삭제</button>
			<button type="button" class="btn btn-info" onclick="location.href='listAll'">목록으로</button>
	</div>

	</div>
	<!-- 글 삭제를 위한 모달 -->
   <div class="modal" id="delModal">
		<div class="modal-dialog">
			<div class="modal-content">

				<!-- Modal Header -->
				<div class="modal-header">
					<h4 class="modal-title">Modal Heading</h4>
					<button type="button" class="btn-close closeModal" data-bs-dismiss="modal"></button>
				</div>
				<!-- Modal body -->
				<div class="modal-body">
				 ${requestScope.board.no }번글을 삭제할까요?
				</div>

				<!-- Modal footer -->
				<div class="modal-footer">
					<button type="button" class="btn btn-success" data-bs-dismiss="modal" onclick="deleteReply();">삭제</button>
					<button type="button" class="btn btn-danger closeModal" data-bs-dismiss="modal">취소</button>
				</div>

			</div>
		</div>
	</div>
	
	<!------------- 댓글 --------------->
	<div class ="replyInputDiv">
		<div class="mb-3 mt-3">
	      <label for="replyText" class="form-label">댓글:</label>
	      <textarea class="form-control" rows="5" style="width: 100%" id="replyText" ></textarea>
  		</div>
	</div>
	<div class="replyBtns mb-3 mt-3">
		<button type="button" class="btn btn-info" onclick="saveReply();">댓글달기</button>
		<button type="button" class="btn btn-secondary">취소</button>
	</div>
	
	
	<div class="allReplies mb-3 mt-3"></div>		
		
	<jsp:include page="../footer.jsp"></jsp:include>
</body>
</html>