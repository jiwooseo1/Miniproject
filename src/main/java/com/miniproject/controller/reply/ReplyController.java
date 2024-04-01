package com.miniproject.controller.reply;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.miniproject.domain.Reply;
import com.miniproject.etc.PagingInfo;
import com.miniproject.service.reply.ReplyService;

@RestController
@RequestMapping("/reply/*")
public class ReplyController {
	
	@Inject
	private ReplyService rService;
	
	@RequestMapping(value="all/{boardNo}/{pageNo}" , method = RequestMethod.GET)
	public ResponseEntity<List<Reply>> getAllReplies(@PathVariable("boardNo") int boardNo,
			@PathVariable("pageNo") int pageNo
			) {
		System.out.println(boardNo + "번 글의" + pageNo + "번 페이지 댓글을 가져오자");
		
		
		ResponseEntity<List<Reply>> result = null;
//		ResponseEntity<Map<String, Object>> result = null;
		
		try {
			// 
//			PagingInfo pi = new PagingInfo();
			
			 List<Reply> lst = rService.getAllReplies(boardNo);
			 result = new ResponseEntity<List<Reply>>(lst, HttpStatus.OK);
			 
//			 Map<String, Object> map = new HashMap<String, Object>();
//			 map.put("replyList", lst);
//			 map.put("pagingInfo", pi);
//			
//			 result = new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
		 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// 예외가 발생하면 json으로 응답할 객체가 없어서, 상태코드만 전송
			result = new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		
		return result;
		
	}
	
	@RequestMapping(value="", method=RequestMethod.POST)
	public ResponseEntity<String> savaReply(@RequestBody Reply newReply) {
		System.out.println(newReply.toString() + "댓글을 저장하자");
		ResponseEntity<String> result = null;
		
		try {
			rService.saveReply(newReply);
			result = new ResponseEntity<String>("success", HttpStatus.OK);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = new ResponseEntity<String>("fail", HttpStatus.FORBIDDEN);
		}
		
		return result;
			
	}
	
//	@RequestMapping(value="/{replyNo}", method =)
	
}
