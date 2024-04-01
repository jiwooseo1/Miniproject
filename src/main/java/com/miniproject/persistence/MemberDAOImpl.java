package com.miniproject.persistence;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.miniproject.domain.LoginDTO;
import com.miniproject.domain.Member;
import com.miniproject.domain.SessionDTO;

@Repository
public class MemberDAOImpl implements MemberDAO {
	
	private static String ns = "com.miniproject.mappers.memberMapper";
		
	@Inject
	private SqlSession ses; // SqlSessionTemplate객체 주입
		
	@Override
	public String getDate() {
		String q = ns + ".curDate";

				
		return ses.selectOne(q);
	
		
	}

	@Override
	public int updateUserPoint(String why, String userId) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("why", why);
		param.put("userId", userId);
		
		return ses.update(ns + ".updateUserPoint", param);
	}

	@Override
	public Member login(LoginDTO tmpMember) throws Exception {
		
		return ses.selectOne(ns + ".login", tmpMember);
	}

	@Override
	public int updateSession(SessionDTO sesDTO) throws Exception {
		
		return ses.update(ns + ".updateSession", sesDTO);
	}

	@Override
	public Member selectAutoLoginUser(String sessionKey) {
		
		return ses.selectOne(ns + ".selectAutoLoginUser", sessionKey);
	}
	
}
