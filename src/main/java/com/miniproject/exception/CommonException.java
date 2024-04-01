package com.miniproject.exception;

import java.io.IOException;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice // 현재 클래스가 공통예외처리를 할 클래스임을 명시
public class CommonException {
	
	@ExceptionHandler(IOException.class)
	public String ioException(Exception e, Model model) {
		// 특정 예외 처리 메서드
		model.addAttribute("errorMsg", e.getMessage()); // 에러 메시지 바인딩
		model.addAttribute("errorStack", e.getStackTrace());
		
		return "commonError";
	}
	
	@ExceptionHandler(Exception.class)
	public String exceptionHandling(Exception e, Model model) {
		
		model.addAttribute("errorMsg", e.getMessage()); // 에러 메시지 바인딩
		model.addAttribute("errorStack", e.getStackTrace());
		
		return "commonError";
	}
	
	
	
}
