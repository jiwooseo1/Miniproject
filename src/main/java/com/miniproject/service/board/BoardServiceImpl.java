package com.miniproject.service.board;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.miniproject.domain.Board;
import com.miniproject.domain.PointLog;
import com.miniproject.domain.ReadCountProcess;
import com.miniproject.domain.SearchCriteria;
import com.miniproject.domain.UploadedFile;
import com.miniproject.etc.PagingInfo;
import com.miniproject.persistence.BoardDAO;
import com.miniproject.persistence.MemberDAO;
import com.miniproject.persistence.PointLogDAO;

@Service
public class BoardServiceImpl implements BoardService {
	
	@Inject
	BoardDAO bDao;
	
	@Inject
	MemberDAO mDao;
	
	@Inject
	PointLogDAO plDao;
	
	@Override
	public List<Board> getEntireBoard() throws Exception {
		
		List<Board> lst = bDao.selectAllBoard();
		
		return lst;
		
		
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveNewBoard(Board newBoard, List<UploadedFile> fileList) throws Exception{
//		게시판 글 저장
//
//		1) board테이블에 글 내용 저장 (insert) 
//
//		if 업로드 파일이 있는경우 
//		   1) 1)번에서 insert된 글의 no값을 얻어와서,
//			 업로드 파일의 갯수만큼 2)번을 반복
//		   2) uploadedfile테이블에 파일 정보를 저장 (insert) 
//		   3) member 테이블에 userpoint를 update
//		   4) pointlog 테이블에 insert	
//
//		if 업로드 파일이 없는경우 
//		   3) member 테이블에 userpoint를 update
//		   4) pointlog 테이블에 insert

// 3) , 4) 번은 공통부분으로 처리		
		
		
		
// 		1) board테이블에 글 내용 저장 (insert) 
		if (bDao.insertNewBoard(newBoard) == 1) {
//			1)번에서 insert된 글의 no값을 얻어와서
			int boardNo = bDao.selectBoardNo();
			System.out.println("저장될 번호: " + boardNo);
			
		  if (fileList.size() > 0) { // 업로드 파일이 있다.
// 			2) uploadedfile테이블에 파일 정보를 저장 (insert)
			for (UploadedFile uf : fileList) {
				System.out.println("테이블에 저장될 uf: " + uf.toString());
				bDao.insertUploadedFile(boardNo, uf);	
			}	
			
		  }
			
//			3) member 테이블에 userpoint를 update
		  mDao.updateUserPoint("게시물작성", newBoard.getWriter()); // why, userId로 전달받음	
		  
//			4) pointlog 테이블에 insert
		  plDao.insertPointLog(new PointLog(-1, null, "게시물작성", 2, newBoard.getWriter()));
		 
			
		}
			
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
	public Map<String, Object> getBoardByNo(int no, String ipAddr) throws Exception {
//		해당 아이피 주소와 글번호 같은 것이 없으면 (→ 해당 아이피주소가 해당 글을 최초로 조회한 경우)
//		→ 아이피 주소와 글번호와 읽은 시간을 readcountprocess 테이블에 insert
//		→ 해당 글번호의 readcount를 증가 (update)
//		→ 해당 글을 가져옴 (select)
//
		
//		해당 아이피 주소와 글번호 같은 것이 있으면
//		1) 시간이 24시간이 지난 경우
//		→ 아이피 주소와 글번호와 읽은 시간을 readcountprocess 테이블에서 update
//		→ 해당 글번호의 readcount를 증가 (update)
//		→ 해당 글을 가져옴 (select)
//
		
//		2) 시간이 24시간이 지나지 않은 경우  
//		→ 해당 글을 가져옴 (select)

		// =========================================
		int readCntResult = -1;
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		// 해당 아이피 주소와 글번호 같은 것이 있는지 없는지
		if(bDao.selectReadCountProcess(no, ipAddr) != null) { // 조회한 적이 있다
		   System.out.println("시간차이: " + bDao.getHourDiffReadTime(no, ipAddr));	
			
		   if (bDao.getHourDiffReadTime(no, ipAddr) > 23) { // 24시간이 지난 경우
			   // 아이피 주소와 글번호와 읽은 시간을 readcountprocess 테이블에서 update
			   if(bDao.updateReadCountProcess(new ReadCountProcess(-1, ipAddr, no, null)) == 1) {
				   //  해당 글번호의 조회수를 +1 증가 (update)
				   readCntResult = bDao.updateReadCount(no);
			   }
			   
		   } else { // 24시간이 지나지 않은 경우
			   readCntResult = 1;
		   }
			
		} else { // 최초 조회
			// 아이피 주소와 글번호와 읽은 시간을 readcountprocess 테이블에 insert
			if (bDao.insertReadCountProcess(new ReadCountProcess(-1, ipAddr, no, null)) == 1) {
				// 해당 글번호의 readcount를 증가 (update)
				readCntResult = bDao.updateReadCount(no);
			}
			
		}
		
		if (readCntResult == 1) {
			// 해당 (no번) 글을 가져옴 (select)
			Board board = bDao.selectBoardByNo(no);
			
			// 업로드한 파일정보를 가져옴 (select)
			List<UploadedFile> upFileList = bDao.selectUploadedFile(no);
			
			result.put("board", board);
			result.put("upFileList", upFileList);

		}
		
		return result;
	}

	@Override
	public Map<String, Object> getEntireBoard(int pageNo) throws Exception {
		PagingInfo pi = getPagingInfo(pageNo);
		System.out.println(pi.toString());
		
		List<Board> lst = bDao.selectAllBoard(pi);
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("boardList", lst);
		returnMap.put("pagingInfo", pi);
		
		return returnMap;
	}

	private PagingInfo getPagingInfo(int pageNo) throws Exception {
		
		PagingInfo result = new PagingInfo();
		
		// pageNo 세팅
		result.setPageNo(pageNo);
		result.setTotalPostCnt(bDao.getTotalPostCnt()); // 전체 게시글 수 저장
		System.out.println("전체 게시글 수 : " + result.getTotalPostCnt());
		
		// 총 페이지수
		result.setTotalPageCnt(result.getTotalPostCnt(), result.getViewPostCntPerPage());
		
		// 보여주기 시작할 row index번호 구하기
		result.setStartRowIndex();
		
		// 전체 페이징 블럭 갯수
		result.setTotalPagingBlockCnt();
		
		// 현재 페이지가 속한 페이징 블럭 번호
		result.setPageBlockofCurrentPage();
		
		// 현재 페이징 블럭 시작 페이지 번호
		result.setStartNumOfCurrentPagingBlock();
		
		// 현재 페이징 블럭 끝 페이지 번호
		result.setEndNumOfCurrentPagingBlock();

		return result;
	}

	@Override
	public Map<String, Object> getEntireBoard(int pageNo, SearchCriteria sc) throws Exception {
		PagingInfo pi = getPagingInfo(pageNo, sc);
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		List<Board> lst = null;
		
		if (!sc.getSearchWord().equals("")) { // 검색어가 있을때
			lst = bDao.selectAllBoard(pi, sc); 
			
		} else if (sc.getSearchWord().equals("")) { // 검색어가 없을때
			lst = bDao.selectAllBoard(pi);
		}
		
		returnMap.put("boardList", lst);
		returnMap.put("pagingInfo", pi);
		
		return returnMap;
	}

	private PagingInfo getPagingInfo(int pageNo, SearchCriteria sc) throws Exception {
		PagingInfo result = new PagingInfo();
		
		// pageNo 세팅
		result.setPageNo(pageNo);
		
		// 전체글의 갯수
		if (!sc.getSearchWord().equals("")) { // 검색어가 있을 때
			result.setTotalPostCnt(bDao.getBoardCntWithSearch(sc));
			
			System.out.println(sc.toString());
			System.out.println(result.getTotalPostCnt());
			
		} else if(sc.getSearchWord().equals("")) { // 검색어가 없을 때
			
			result.setTotalPostCnt(bDao.getTotalPostCnt());
		}
		
		// 총 페이지수
		result.setTotalPageCnt(result.getTotalPostCnt(), result.getViewPostCntPerPage());
		
		// 보여주기 시작할 row index번호 구하기
		result.setStartRowIndex();
		
		// 전체 페이징 블럭 갯수
		result.setTotalPagingBlockCnt();
		
		// 현재 페이지가 속한 페이징 블럭 번호
		result.setPageBlockofCurrentPage();
		
		// 현재 페이징 블럭 시작 페이지 번호
		result.setStartNumOfCurrentPagingBlock();
		
		// 현재 페이징 블럭 끝 페이지 번호
		result.setEndNumOfCurrentPagingBlock();
		
		
		return result;
	}

	@Override
	public String getBoardWriterByNo(int no) throws Exception {
		
		
		
		
		return bDao.getWriterByNo(no);
		
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean likeBoard(int boardNo, String who) throws Exception {
		boolean result = false;
		
		if (bDao.likeBoard(boardNo, who) == 1) {
			// board테이블에 likecount 를 업데이트 (+1)
			if (this.modifyBoardLikeCount(1, boardNo) == 1) {
				result = true;
			}
			
		}
		
		return result;
	}


	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean dislikeBoard(int boardNo, String who) throws Exception {
		boolean result = false;
		
		if (bDao.dislikeBoard(boardNo, who) == 1) {
			// board테이블에 likecount 를 업데이트 (-1)
			if (this.modifyBoardLikeCount(-1, boardNo) == 1) {
				result = true;
			}
		}
		
		return result;
	}
	
	private int modifyBoardLikeCount(int n, int boardNo) throws Exception {
		return bDao.updateBoardLikeCount(n, boardNo);
		
	}

	@Override
	public List<String> getPeopleWhoLikesBoard(int boardNo) throws Exception {
		
		return bDao.selectPeopleWhoLikesBoard(boardNo);
	}

	

	
}
