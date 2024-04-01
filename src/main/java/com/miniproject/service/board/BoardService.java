package com.miniproject.service.board;

import java.util.List;
import java.util.Map;

import com.miniproject.domain.Board;
import com.miniproject.domain.SearchCriteria;
import com.miniproject.domain.UploadedFile;

public interface BoardService {
	
	// 전체 게시글 조회
	List<Board> getEntireBoard() throws Exception;
	
	// 게시글 저장
	void saveNewBoard(Board newBoard, List<UploadedFile> fileList) throws Exception;
	
	// no번 게시글 상세조회
	Map<String, Object> getBoardByNo(int no, String ipAddr) throws Exception;
	
	// 페이지 번호에 해당하는 게시글 전체 조회
	Map<String, Object> getEntireBoard(int pageNo) throws Exception;
	
	// 게시글 조회 (pageInfo + 검색어)
	Map<String, Object> getEntireBoard(int pageNo, SearchCriteria sc) throws Exception;
	
	// 게시글의 작성자 조회
	String getBoardWriterByNo(int no) throws Exception;
	
	// 좋아요
	boolean likeBoard(int boardNo, String who) throws Exception;
	
	// 싫어요
	boolean dislikeBoard(int boardNo, String who) throws Exception;
	
	// 좋아요 누른 사람들 목록 조회
	List<String> getPeopleWhoLikesBoard(int boardNo) throws Exception;
}
