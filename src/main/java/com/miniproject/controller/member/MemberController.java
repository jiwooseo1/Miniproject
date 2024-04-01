package com.miniproject.controller.member;

import javax.inject.Inject;
import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.WebUtils;

import com.miniproject.domain.LoginDTO;
import com.miniproject.domain.Member;
import com.miniproject.domain.SessionDTO;
import com.miniproject.etc.SessionCheck;
import com.miniproject.interceptor.LoginInterceptor;
import com.miniproject.service.member.MemberService;

@Controller
@RequestMapping("/member/*")
public class MemberController {
	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	@Inject
	MemberService mService;
	
	@RequestMapping("login")
	public void loginGET() {
		// LoginInterceptor의 preHandle() 호출 후...
		System.out.println("loginGET방식 호출됨");
		
		// /member/login.jsp 가 반환
	}
	
	@RequestMapping(value = "login", method = RequestMethod.POST)
	public String loginPOST(LoginDTO tmpMember, Model model, RedirectAttributes rttr) throws Exception {
		
		System.out.println(tmpMember.toString() + "으로 로그인 해보자.");
		
		Member loginMember = mService.login(tmpMember);
		
		if (loginMember != null) {
			System.out.println("로그인 성공!");
			model.addAttribute("loginMember", loginMember);
			return "index";
			
		} else {
			System.out.println("로그인 실패");
//			rttr.addAttribute("status", "fail"); // 쿼리스트링으로 전달
			rttr.addFlashAttribute("status", "fail"); // 쿼리스트링으로 전달X
			
			return "redirect:login";
		}
		
		
	}
	
	@RequestMapping(value="logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest req, HttpSession ses, HttpServletResponse resp) throws Exception {
		HttpSession reqSes = req.getSession();
		
		System.out.println("로그아웃 : " + reqSes.getId());
		
//		if (ses.getAttribute("loginUser") != null) {
//			ses.removeAttribute("loginUser");
//			ses.invalidate();
//	
//		}
		
		// 자동로그인 쿠키 삭제 + member테이블 수정
		Cookie loginCookie = WebUtils.getCookie(req, "loginCookie");
		
		if (loginCookie != null) { // 자동로그인 쿠키가 있다면
			loginCookie.setMaxAge(0); // 만료일을 0으로 함으로써 쿠키 삭제
			loginCookie.setPath("/");
			
			resp.addCookie(loginCookie);
			
			mService.remember(new SessionDTO(((Member)ses.getAttribute("loginUser")).getUserId(), null, null));
			
		}
		
		
		
		
		
		// 로그아웃 할 때, 세션map에 담겨진 세션 제거
		if ((Member)ses.getAttribute("loginUser") != null) {
			SessionCheck.removeKey(((Member)ses.getAttribute("loginUser")).getUserId());
		}
		
		return "redirect:/";
			
	}
	
}
