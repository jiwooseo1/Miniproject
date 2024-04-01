package com.miniproject.service.member;

import com.miniproject.domain.LoginDTO;
import com.miniproject.domain.Member;
import com.miniproject.domain.SessionDTO;

public interface MemberService {
	
	// 로그인
	Member login(LoginDTO tmpMember) throws Exception;
	
	// 자동로그인 유저 정보를 저장
	boolean remember(SessionDTO sessionDTO) throws Exception;
	
	// 자동 로그인 유저 체크
	Member checkAutoLoginUser(String sessionKey) throws Exception;

}
