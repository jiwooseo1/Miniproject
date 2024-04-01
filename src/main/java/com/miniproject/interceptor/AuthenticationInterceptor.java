package com.miniproject.interceptor;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.miniproject.domain.Member;
import com.miniproject.etc.DestinationPath;
import com.miniproject.service.board.BoardService;


public class AuthenticationInterceptor extends HandlerInterceptorAdapter {
	
	@Inject
	private BoardService bService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		boolean result = false;
		
		System.out.println("로그인을 했는지 안했는지 검사하러 옴-- AuthenticationInterceptor - preHandle()");
		
		DestinationPath.savePrePath(request);
		
		HttpSession ses = request.getSession();
		
		if (ses.getAttribute("loginUser") != null) { // 로그인 했다. 
			 
			System.out.println("로그인 했음 --> 가던 길 가게하자!");
			
			
			String uri = request.getRequestURI();
			
			// 로그인한 유저와 글작성자 확인
			// 1) writer정보를 쿼리스트링이 아닌, service -> dao 거쳐서 writer정보를 가져오는 방법
			
//			bService.getBoardWriterByNo(no);
			
			if (uri.contains("/modifyBoard") || uri.contains("/remBoard")) {
				int no = Integer.parseInt(request.getParameter("no"));
				String loginUser = ((Member)ses.getAttribute("loginUser")).getUserId();
				String writer = bService.getBoardWriterByNo(no);
				
				if (!writer.equals(loginUser)) {
					response.sendRedirect("viewBoard?no=" + request.getParameter("no") + "&status=noPermission");
					return false;
				}
				
			}
			
			
//			// 수정버튼에 writer정보를 쿼리스트링으로 추가하여 처리하는 경우
//			String writer = request.getParameter("writer");
//			
//			System.out.println("수정하러 갈 작성자 : " + writer);
//			System.out.println("로그인한 유저 : " + ((Member)ses.getAttribute("loginUser")).getUserId());
//			
//			Member loginMember = (Member)ses.getAttribute("loginUser");
//			if (uri.indexOf("/modifyBoard") != -1 || uri.indexOf("/remBoard") != -1) {
//				// 수정/삭제 경우: 로그인userId == 작성자writer 조건을 만족해야 함.
//				System.out.println("수정하러 왔고, 로그인 되어 있음");
//				
//				if (!loginMember.getUserId().equals("writer")) {
//					response.sendRedirect("viewBoard?no=" + request.getParameter("no") + "&writer=" + writer +
//							"&status=noPermission");
//					return false;
//				}
//				
//			}
			
			
			result = true;
			
			
		} else { // 로그인 안했음
			System.out.println("로그인 안했음 --> 로그인 하러 가자!");
			response.sendRedirect("/member/login");
			
		}
		
		return result; 
	}
	
}
