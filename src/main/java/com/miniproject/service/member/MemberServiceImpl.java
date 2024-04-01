package com.miniproject.service.member;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.miniproject.domain.LoginDTO;
import com.miniproject.domain.Member;
import com.miniproject.domain.PointLog;
import com.miniproject.domain.SessionDTO;
import com.miniproject.persistence.MemberDAO;
import com.miniproject.persistence.PointLogDAO;

@Service
public class MemberServiceImpl implements MemberService {
	
	@Inject
	MemberDAO mDao;
	
	@Inject
	PointLogDAO pDao;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Member login(LoginDTO tmpMember) throws Exception {
		
		System.out.println("MemberServiceImpl : 로그인 처리 하자" + tmpMember.toString());
		
		// 1) 로그인 한다. (member 테이블에서 userId, userPwd가 일치여부 확인)
			Member loginMember = mDao.login(tmpMember);
		    
		    if(loginMember != null) {
		    	System.out.println("로그인한 멤버 정보: " + loginMember.toString());
		    	// 2) 로그인 성공하면 포인트 부여(Member테이블 포인트 update)
		    	if(mDao.updateUserPoint("로그인", loginMember.getUserId()) == 1 ) {
		    		// 3) pointlog테이블에 저장 insert
		    		pDao.insertPointLog(new PointLog(-1, null, "로그인", 5, loginMember.getUserId()));
		    	}
		    	
		    	
		    }
		
		    
		return loginMember;
	}

	@Override
	public boolean remember(SessionDTO sesDTO) throws Exception {
		boolean result = false;
		
		if (mDao.updateSession(sesDTO) == 1) {
			result = true;
		}
		
		return result;
	}

	@Override
	public Member checkAutoLoginUser(String sessionKey) throws Exception {
		
		return mDao.selectAutoLoginUser(sessionKey);

	}
}
