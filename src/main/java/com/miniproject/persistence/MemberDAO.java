package com.miniproject.persistence;

import com.miniproject.domain.LoginDTO;
import com.miniproject.domain.Member;
import com.miniproject.domain.SessionDTO;

public interface MemberDAO {

	String getDate();
	
	// member 테이블에 userpoint를 update
	int updateUserPoint(String why, String userId) throws Exception;
	
	// 로그인 
	Member login(LoginDTO tmpMember) throws Exception;
	
	// 자동로그인 정보를 저장
	int updateSession(SessionDTO sesDTO) throws Exception;

	// 자동 로그인 유저 체크
	Member selectAutoLoginUser(String sessionKey);
	
	
}
