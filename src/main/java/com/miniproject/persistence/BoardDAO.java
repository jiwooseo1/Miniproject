package com.miniproject.persistence;

import java.util.List;

import com.miniproject.domain.Board;
import com.miniproject.domain.ReadCountProcess;
import com.miniproject.domain.SearchCriteria;
import com.miniproject.domain.UploadedFile;
import com.miniproject.etc.PagingInfo;

public interface BoardDAO {
	

	// 전체 게시글 조회
	List<Board> selectAllBoard() throws Exception;
	
	// 게시글 저장
	int insertNewBoard(Board newBoard) throws Exception;
	
	// 저장된 게시글의 no값을 얻어오기
	int selectBoardNo() throws Exception;
	
	// 업로드 파일 정보를 저장
	void insertUploadedFile(int boardNo, UploadedFile uf) throws Exception;
	
	// 해당 아이피 주소와 글번호 같은 것이 있는지 없는지 체크
	ReadCountProcess selectReadCountProcess(int no, String ipAddr) throws Exception;
	
	// ipAddr가 no 글을 읽은지 24시간이 지났는지 아닌지 (시간차이)
	int getHourDiffReadTime(int no, String ipAddr) throws Exception;
	
	// readcountprocess 테이블에서 update
	int updateReadCountProcess(ReadCountProcess rct) throws Exception;
	
	// 조회수 증가
	int updateReadCount(int no) throws Exception;
	
	// readcountprocess 테이블에 insert
	int insertReadCountProcess(ReadCountProcess rct) throws Exception;
	
	// no번 글 조회
	Board selectBoardByNo(int no) throws Exception;
	
	// no번글 리스트 가져오기
	List<UploadedFile> selectUploadedFile(int no) throws Exception;
	
	// 전체 게시글 수
	int getTotalPostCnt() throws Exception;
	
	// 전체 게시글 조회( + pagingInfo)
	List<Board> selectAllBoard(PagingInfo pi) throws Exception;
	
	// 전체 게시글 수 (검색어가 있을 때)
	int getBoardCntWithSearch(SearchCriteria sc) throws Exception;
	
	// 전체 게시글 조회( + pagingInfo + 검색어)
	List<Board> selectAllBoard(PagingInfo pi, SearchCriteria sc) throws Exception;
	
	// 게시글의 작성자 조회
	String getWriterByNo(int no) throws Exception;
	
	// 좋아요
	int likeBoard(int boardNo, String who) throws Exception;
	
	// 싫어요
	int dislikeBoard(int boardNo, String who) throws Exception;
	
	// board테이블의 좋아요/안좋아요 업데이트
	int updateBoardLikeCount(int n, int boardNo) throws Exception;

	// 좋아요 누른 사람들 목록 조회
	List<String> selectPeopleWhoLikesBoard(int boardNo) throws Exception;
	

}
