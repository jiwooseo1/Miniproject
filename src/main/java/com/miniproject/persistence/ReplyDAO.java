package com.miniproject.persistence;

import java.util.List;

import com.miniproject.domain.Reply;

public interface ReplyDAO {
	// 모든 댓글 조회
	List<Reply> selectAllReplies(int boardNo) throws Exception;
	
	// 새댓글 저장
	int insertNewReply(Reply newReply) throws Exception;
}
