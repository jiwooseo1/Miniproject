package com.miniproject.service.reply;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.miniproject.domain.PointLog;
import com.miniproject.domain.Reply;
import com.miniproject.persistence.MemberDAO;
import com.miniproject.persistence.PointLogDAO;
import com.miniproject.persistence.ReplyDAO;

@Service
public class ReplyServiceImpl implements ReplyService {
	
	@Inject
	ReplyDAO rDao;
	
	@Inject
	MemberDAO mDao;
	
	@Inject
	PointLogDAO pDao;
	
	@Override
	public List<Reply> getAllReplies(int boardNo) throws Exception {
	 	List<Reply> lst = rDao.selectAllReplies(boardNo);
		
		return lst;
	}

	@Override
	@Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
	public boolean saveReply(Reply newReply) throws Exception {
		boolean result = false;
//		1) 새댓글 저장 (insert)
		if(rDao.insertNewReply(newReply) == 1) {
//		2) point부여 (답글작성, 1점) (member테이블 userPoint update한다)
		if(mDao.updateUserPoint("답글작성", newReply.getReplier()) == 1) {
//		3) pointlog 기록 (pointlog테이블에 insert한다)
			if(pDao.insertPointLog(new PointLog(-1, null, "답글작성", 1, newReply.getReplier())) == 1) {
				result = true;
				
			}
			
			}
			
		}
		
		
		
		return result;
	}

}
