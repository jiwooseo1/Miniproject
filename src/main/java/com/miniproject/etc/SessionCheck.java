package com.miniproject.etc;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;


@WebListener
public class SessionCheck implements HttpSessionListener {

	private static Map<String, HttpSession> sessions = new ConcurrentHashMap<String, HttpSession>();
	
	public static synchronized void replaceSessionKey (HttpSession ses, String loginUserId) {
		// 세션 리스트에 로그인 유저아이디가 없고 ses값만 있으면 -> 최초 로그인
		if (!sessions.containsKey(loginUserId) && sessions.containsValue(ses)) {
			System.out.println(loginUserId + "로 최초 로그인");
			sessions.put(loginUserId, ses);
			sessions.remove(ses.getId());
		} else if (sessions.containsKey(loginUserId)) {
			System.out.println(loginUserId + "로 중복 로그인하려고 함");
			removeKey(loginUserId);
			sessions.put(loginUserId, ses);
			
		}
		
		printSessionsMap();
	}
	
	public static void removeKey(String userId) {
		if (sessions.containsKey(userId)) {
			sessions.get(userId).removeAttribute("loginUser");
			
			sessions.get(userId).invalidate();
			sessions.remove(userId);
		}
		
		printSessionsMap();
		
	}

	@Override
	public synchronized void sessionCreated(HttpSessionEvent se) {
		System.out.println("세션이 생성됨!!!");
		System.out.println("생성된 세션 id: " + se.getSession().getId());
		
		// 세션이 생성되면 Map에 해당 세션을 등록 
		sessions.put(se.getSession().getId(), se.getSession());
		
		printSessionsMap();
	}


	@Override
	public synchronized void sessionDestroyed(HttpSessionEvent se) {
		System.out.println("세션이 종료됨!!!" + se.getSession().getId());
		// 세션이 종료되면 Map에서도 해당 세션 삭제
		if (sessions.containsKey(se.getSession().getId())) {
			se.getSession().invalidate();
			sessions.remove(se.getSession().getId());
		}
		
	}
	
	
	private static void printSessionsMap() {
		System.out.println("============= 현재 생성된 세션 리스트 ===========");
		Set<String> keys = sessions.keySet();
		
		for (String key : keys) {
			System.out.println(key + ": " + sessions.get(key).toString());
		}
		System.out.println("===================================================");
	}

}
