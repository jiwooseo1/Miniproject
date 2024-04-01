package com.miniproject.controller.board;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.miniproject.domain.Board;
import com.miniproject.domain.SearchCriteria;
import com.miniproject.domain.UploadedFile;
import com.miniproject.etc.GetUserIPAddr;
import com.miniproject.etc.PagingInfo;
import com.miniproject.etc.UploadFileProcess;
import com.miniproject.service.board.BoardService;

/**
 * @packageName : com.miniproject.controller.board
 * @fileName : BoardController.java
 * @author : jkw
 * @date : 2024. 1. 22.
 * @description : 
 */
@Controller
@RequestMapping("/board/*")
public class BoardController {
	
	@Inject
	BoardService bService; // BoardService 객체 주입
	
	private static final Logger logger = LoggerFactory.getLogger(BoardController.class);
	
	private List<UploadedFile> fileList = new ArrayList<UploadedFile>();
	
	// 게시판 전체 목록 조회 (paging)
	@RequestMapping("listAll")
	public void listAll(Model model, @RequestParam(value="no", defaultValue = "1") int pageNo,
			@RequestParam(value="searchType", defaultValue = "") String searchType, 
			@RequestParam(value="searchWord", defaultValue = "") String searchWord) throws Exception 
			 { // void로 해놓으면 매핑해놓은 주소로 요청됨 listAll.jsp로
		
		logger.info(pageNo + "페이지의 게시판 글조회가 호출됨");
		System.out.println("검색타입: " + searchType + ",검색어: " + searchWord);
		
		SearchCriteria sc = new SearchCriteria(searchWord, searchType);
		
		Map<String, Object>	map = bService.getEntireBoard(pageNo, sc);
		
		model.addAttribute("boardList", (List<Board>)map.get("boardList")); // 바인딩
		model.addAttribute("pagingInfo", (PagingInfo)map.get("pagingInfo"));
		
	}
	
	@RequestMapping("writeBoard")
	public void showWriteBoard(HttpSession ses) {
		logger.info("writeBoard가 호출됨");
		
		String uuid = UUID.randomUUID().toString();
		
		ses.setAttribute("csrfToken", uuid); // 세션에 바인딩
		
		
		
	}
	
	@RequestMapping(value="writeBoard", method=RequestMethod.POST)
	public String writeBoard(Board newBoard, @RequestParam("csrfToken") String inputcsrf, HttpSession ses ) {
		
		logger.info("게시판 글 작성 : " + newBoard.toString() );
		logger.info("csrf : " + inputcsrf);
		
		String redirectPage = "";
		if (( (String)ses.getAttribute("csrfToken")).equals(inputcsrf)) {
			// csrfToken이 같은 경우에만 게시글을 저장
			
			try {
				bService.saveNewBoard(newBoard, fileList);
				redirectPage = "listAll";
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				redirectPage = "listAll?status=fail";
				
			}
			
		}
		
		return "redirect:" + redirectPage;
		
	}
	/**
	 * @MethodName : uploadFile
	 * @author : jkw
	 * @date : 2024. 1. 22.
	 * @description :		
	 * @param uploadFile
	 * @param req
	 */
	@RequestMapping(value="uploadFile" , method=RequestMethod.POST)
	public @ResponseBody List<UploadedFile> uploadFile(MultipartFile uploadFile, HttpServletRequest req) {
		logger.info("파일을 업로드함");
		
		System.out.println("파일의 오리지널이름: " + uploadFile.getOriginalFilename());
		System.out.println("파일의 사이즈: " + uploadFile.getSize());
		System.out.println("파일의 ContentType: " + uploadFile.getContentType());
		
		// 파일이 실제로 저장될 경로 realPath
		String realPath = req.getSession().getServletContext().getRealPath("resources/uploads");
		System.out.println("realPath : " + realPath);
		
		// 파일처리
		UploadedFile uf = null;
		try {
			uf = UploadFileProcess.fileUpload(uploadFile.getOriginalFilename(), uploadFile.getSize(),
					uploadFile.getContentType(), uploadFile.getBytes(), realPath);
			
			if (uf != null) {
				fileList.add(uf);
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (UploadedFile f : this.fileList) {
			System.out.println("현재 파일 업로드 리스트: " + f.toString());
		}
		
		return fileList;
		
	}
	
	@RequestMapping("remFile")
	public ResponseEntity<String> removeFile(@RequestParam("removeFile") String remFile, HttpServletRequest req) {
		
		System.out.println(remFile + "을 삭제하자!");
		ResponseEntity<String> result = null;
		
		String realPath = req.getSession().getServletContext().getRealPath("resources/uploads");
		System.out.println("realPath : " + realPath);
		UploadFileProcess.deleteFile(fileList, remFile, realPath);
		
		
		// fileList에서 remFile을 삭제하기 (가변 배열삭제)
		// 0, 1, 2을 삭제한다고치면 1삭제를 누르면 2는 앞으로 땡겨져서 1번째가 된다
		int ind = 0;
		for (UploadedFile uf : fileList) { 
			 if(!remFile.equals(uf.getOriginalFileName())) {
				 ind++;	
			} else if (remFile.equals(uf.getOriginalFileName())) {
				break;
			}
		}
		
			System.out.println("삭제할 index: " + ind);
			
			fileList.remove(ind);
			
			result = new ResponseEntity<String>("success", HttpStatus.OK);
			
			return result;
			
	}
	
	@RequestMapping("remAllFile")
	public ResponseEntity<String> remAllFile(HttpServletRequest req) {
		String realPath = req.getSession().getServletContext().getRealPath("resources/uploads");
		
		UploadFileProcess.deleteAllFile(fileList, realPath);
		
		fileList.clear();
		
		return new ResponseEntity<String>("success", HttpStatus.OK);
		
	}
	
	@RequestMapping("viewBoard")
	public void viewBoard(@RequestParam("no") int no, HttpServletRequest req, Model model) throws Exception { // 쿼리를달고 왔으니 리퀘스트파람으로 얻어온다
		logger.info(no + "번 글을 상세조회하자!");
		
		Map<String, Object> result = bService.getBoardByNo(no, GetUserIPAddr.getIp(req));
		
		model.addAttribute("board", (Board)result.get("board"));
		model.addAttribute("upFileList", (List<UploadedFile>)result.get("upFileList"));
		
		// 좋아요 한 사람들 바인딩
		model.addAttribute("likePeople", bService.getPeopleWhoLikesBoard(no));
		
		
	}
	
	@RequestMapping("modifyBoard")
	public void modifyBoard(@RequestParam("no") int no) {
		
		System.out.println(no + "번 글을 수정하자");
		
	}
	
	@RequestMapping("remBoard")
	public void removeBoard(@RequestParam("no") int no, @RequestParam("writer") String writer) {
		System.out.println(no + "번 글을 삭제하자");
	}
	
	@RequestMapping(value = "likedislike", method = RequestMethod.POST)
	public ResponseEntity<String> likeDislike(@RequestParam("who") String who,
			@RequestParam("boardNo") int boardNo,
			@RequestParam("behavior") String behavior) {
		
		ResponseEntity<String> result = null;
		
		logger.info(who + "님이 " + boardNo + "번 글을 " + behavior + "하신다고 합니다." );
		
		boolean dbResult = false;
		
		try {
			
			if (behavior.equals("like")) {
				dbResult = bService.likeBoard(boardNo, who);
			} else if (behavior.equals("dislike")) {
				dbResult = bService.dislikeBoard(boardNo, who);
			}
			
			if (dbResult) { 
				result = new ResponseEntity<String>("success", HttpStatus.OK);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = new ResponseEntity<String>("fail", HttpStatus.CONFLICT);
		}
		
		
		return result;
		
	}
	
}
