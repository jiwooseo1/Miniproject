package com.miniproject.interceptor;

import java.sql.Timestamp;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;

import com.miniproject.domain.Member;
import com.miniproject.domain.SessionDTO;
import com.miniproject.etc.SessionCheck;
import com.miniproject.service.member.MemberService;

// 제어를 빼앗아 실제 로그인을 처리하는 interceptor
public class LoginInterceptor extends HandlerInterceptorAdapter {
	
	@Inject
	private MemberService service;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("LoginInterceptor - preHandle() : 로그인처리 하러왔음...");
		
		boolean showLoginPage = false;
		
		// 자동로그인을 체크한 유저에 대해 로그인 처리
		// 1) 쿠키가 있는지 없는지 검사
		Cookie loginCookie = WebUtils.getCookie(request, "loginCookie");
		System.out.println("자동로그인 쿠키 loginCookie : " + loginCookie);
		
		if (loginCookie != null) { // 쿠키가 있으면
			
			String cookieValue = loginCookie.getValue(); // 쿠키에 저장된 세션아이디
			
//			-> 쿠키에 저장된 세션아이디와 DB에 저장된 sessionKey가 같은지 비교
//			-> DB에 저장된 sessionLimit가 현재시간보다 큰지 비교
			Member autoLoginUser = service.checkAutoLoginUser(cookieValue);
			
			if (autoLoginUser != null) { // 자동로그인 처리해야함
				System.out.println("자동로그인 할 유저 : " + autoLoginUser.getUserId());
				
				// 중복로그인 체크
				SessionCheck.replaceSessionKey(request.getSession(), autoLoginUser.getUserId());
				
				// 로그인 처리
				WebUtils.setSessionAttribute(request, "loginUser", autoLoginUser);
				
				System.out.println("자동로그인 되었습니다. : " + ((Member)WebUtils.getSessionAttribute(request, "loginUser")));
				
				
				
				if (WebUtils.getSessionAttribute(request, "returnPath") != null) {
					response.sendRedirect((String)WebUtils.getSessionAttribute(request, "returnPath"));
				} else {
					response.sendRedirect("/");
				}
			}
			
		} if (request.getMethod().equals("GET") && request.getParameter("redirectUrl") != null) { 
			// 댓글작성 로그인 (이전경로저장) 처리
			// GET방식, 쿼리스트링에 redirectUrl이 존재하는 경우
			if (!request.getParameter("redirectUrl").equals("")) {
				if (request.getParameter("redirectUrl").contains("viewBoard")) {
					// 댓글 달려다가 로그인하러 끌려온 경우
					
					String uri = "/board/viewBoard";
					String queryStr = "?no=" + request.getParameter("no");
					
					request.getSession().setAttribute("returnPath", uri + queryStr);
					
				}
			}
			
			showLoginPage = true;
			
		} else {
			showLoginPage = true;
		}
		
		
		return showLoginPage;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
		System.out.println("LoginInterceptor - postHandle() : 로그인처리하러 DB다녀왔음");
		
		ModelMap modelMap = modelAndView.getModelMap();
		Member loginMember = (Member)modelMap.get("loginMember");
		
		HttpSession ses = request.getSession();
		
		if (loginMember != null) { // 로그인 성공하면 session에 로그인 기록을 남김.
			System.out.println("현재 로그인한 유저 : " + loginMember.toString());
			ses.setAttribute("loginUser", loginMember);
			
			// 중복로그인 체크
			SessionCheck.replaceSessionKey(ses, loginMember.getUserId());
			
			// 자동로그인 처리
//			자동 로그인 체크한 유저이면 (on이 전달되면 또는 null이 아닌경우)
//			-> 쿠키생성 (loginCookie = sessionId)
//			-> DB member테이블에 sessionLimit, sessionKey를 저장 (update)
			System.out.println("자동로그인 체크 여부 : " + request.getParameter("remember"));
			
			if (request.getParameter("remember") != null) {
				System.out.println("자동로그인 유저입니다......");
//				1) 쿠키생성 (loginCookie = sessionId, 만료일 : 일주일)
				String sessionValue = ses.getId();
				Timestamp sesLimit = new Timestamp(System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000));
				
				Cookie loginCookie = new Cookie("loginCookie", sessionValue); // 쿠키 객체 생성
				loginCookie.setMaxAge(7 * 24 * 60 * 60); // 초(seconds) 단위
				loginCookie.setPath("/");
				
				
				if (service.remember(new SessionDTO(loginMember.getUserId(), sesLimit, sessionValue))) {
					response.addCookie(loginCookie); // 쿠키 저장
					
				}
				
				
			}
			
			
			// 로그인 성공 후 돌아갈 경로 처리
			String returnPath = "";
			
			if(ses.getAttribute("returnPath") != null) {
				returnPath = (String)ses.getAttribute("returnPath");
			}
			
			
			
			response.sendRedirect(!returnPath.equals("")? returnPath : "/"); 
		}
		
	}
	
}
