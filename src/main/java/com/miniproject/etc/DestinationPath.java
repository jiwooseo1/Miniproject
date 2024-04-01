package com.miniproject.etc;

import javax.servlet.http.HttpServletRequest;

public class DestinationPath {
	
	public static void savePrePath(HttpServletRequest request) {
		// 강제로 로그인하러 온 경우, 검사하는 곳에서 이전에 있던 경로를 남기기 위한 메서드
		String uri = request.getRequestURI();
		System.out.println("uri: " + uri);
		
		// 쿼리스트링이 있는 경우 ~~~~~~~~~~
		System.out.println("쿼리스트링이 있나???" + request.getQueryString());
		
		String queryStr = "";
		if (request.getQueryString() != null) { // 쿼리스트링이 있다면
			queryStr = request.getQueryString(); // ?를 제외한 쿼리스트링 문자열
		} 
		
		
		if (!queryStr.equals("")) {
			queryStr = "?" + queryStr;
		}
		
		
		if (request.getMethod().equals("GET")) { // GET방식으로 요청이 되었다면
			System.out.println("destination: " + uri + queryStr); // 쿼리스트링 있다면
			
			// 세션에 바인딩 (저장)
			request.getSession().setAttribute("returnPath", uri + queryStr);
			
		}
		
		
		
	}
}
