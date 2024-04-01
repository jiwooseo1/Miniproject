package com.miniproject.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.miniproject.domain.Board;
import com.miniproject.domain.ReadCountProcess;
import com.miniproject.domain.SearchCriteria;
import com.miniproject.domain.UploadedFile;
import com.miniproject.etc.PagingInfo;

@Repository
public class BoardDAOImpl implements BoardDAO {
	
	private static String ns = "com.miniproject.mappers.boardMapper";
	
	@Inject
	private SqlSession ses; // sessionTemplate 객체 주입
	
	@Override
	public List<Board> selectAllBoard() throws Exception {
		
		String q = ns + ".getAllBoard";
		return ses.selectList(q);
	}

	@Override
	public int insertNewBoard(Board newBoard) throws Exception {
		
		return ses.insert(ns + ".insertNewBoard", newBoard);
		
	}

	@Override
	public int selectBoardNo() throws Exception {

		return ses.selectOne(ns + ".getNo");
	}

	@Override
	public void insertUploadedFile(int boardNo, UploadedFile uf) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("originalFileName", uf.getOriginalFileName());
		param.put("newFileName", uf.getNewFileName());
		param.put("fileSize", uf.getSize());
		param.put("boardNo", boardNo);
		param.put("thumbFileName", uf.getThumbFileName());
		
		
		ses.insert(ns + ".insertUploadedFile", param);
		
		
	}

	@Override
	public ReadCountProcess selectReadCountProcess(int no, String ipAddr) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("boardNo", no);
		param.put("ipAddr", ipAddr);
		
		return ses.selectOne(ns + ".getReadCountProcess", param);
		
	}

	@Override
	public int getHourDiffReadTime(int no, String ipAddr) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("ipAddr", ipAddr);
		param.put("no", no);

		return ses.selectOne(ns + ".getHourDiffReadTime", param);
		
	}

	@Override
	public int updateReadCountProcess(ReadCountProcess rcp) throws Exception {
		
		
		return ses.update(ns + ".updateReadCountProcess", rcp);
	}

	@Override
	public int updateReadCount(int no) throws Exception {
		
		return ses.update(ns + ".updateReadCount", no);
	}

	@Override
	public int insertReadCountProcess(ReadCountProcess rct) throws Exception {
		
		return ses.insert(ns + ".insertReadCountProcess", rct);
	}

	@Override
	public Board selectBoardByNo(int no) {
		
		return ses.selectOne(ns + ".getBoardByNo", no);
	}

	@Override
	public List<UploadedFile> selectUploadedFile(int no) throws Exception {
		
		return ses.selectList(ns + ".getUploadedFiles", no);
	}

	@Override
	public int getTotalPostCnt() throws Exception {
		
		return ses.selectOne(ns + ".getBoardCnt");
	}

	@Override
	public List<Board> selectAllBoard(PagingInfo pi) throws Exception {
		
		
		return ses.selectList(ns + ".getAllBoard", pi);
	}

	@Override
	public int getBoardCntWithSearch(SearchCriteria sc) throws Exception {
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("searchType", sc.getSearchType());
		param.put("searchWord", "%" + sc.getSearchWord() + "%");
		
		return ses.selectOne(ns + ".getTotalBoardCntWithSearch", param);
	}

	@Override
	public List<Board> selectAllBoard(PagingInfo pi, SearchCriteria sc) throws Exception {
		
		Map<String, Object> param = new HashMap<String, Object>();
		
		param.put("searchType", sc.getSearchType());
		param.put("searchWord", "%" + sc.getSearchWord() + "%");
		param.put("startRowIndex", pi.getStartRowIndex());
		param.put("viewPostCntPerPage", pi.getViewPostCntPerPage());
		
		
		return ses.selectList(ns + ".getAllBoardWithSearch", param);
	}

	@Override
	public String getWriterByNo(int no) throws Exception {
		
		return ses.selectOne(ns + ".getWriterByNo", no);
	}

	@Override
	public int likeBoard(int boardNo, String who) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("who", who);
		param.put("boardNo", boardNo);
		
		return ses.insert(ns + ".like", param);
	}

	@Override
	public int dislikeBoard(int boardNo, String who) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("who", who);
		param.put("boardNo", boardNo);
		
		
		return ses.delete(ns + ".dislike", param);
	}

	@Override
	public int updateBoardLikeCount(int n, int boardNo) throws Exception {
		Map<String, Integer> param = new HashMap<String, Integer>();
		param.put("n", n);
		param.put("boardNo", boardNo);
		
		return ses.update(ns + ".incDecLikeCount", param);
	}

	@Override
	public List<String> selectPeopleWhoLikesBoard(int boardNo) throws Exception {
		
		return ses.selectList(ns + ".getPeopleWhoLikesBoard", boardNo);
	}

	
}
