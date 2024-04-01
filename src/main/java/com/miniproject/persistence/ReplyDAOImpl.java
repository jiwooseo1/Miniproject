package com.miniproject.persistence;

import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.miniproject.domain.Reply;

@Repository
public class ReplyDAOImpl implements ReplyDAO {

	private static String ns = "com.miniproject.mappers.replyMapper";
	
	@Inject
	private SqlSession ses;
	
	@Override
	public List<Reply> selectAllReplies(int boardNo) throws Exception {
		
		return ses.selectList(ns + ".getAllReplies" , boardNo);
	}

	@Override
	public int insertNewReply(Reply newReply) throws Exception {
		
		return ses.insert(ns + ".insertReply", newReply);
	}

}
