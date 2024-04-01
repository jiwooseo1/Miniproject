package com.miniproject.aop;

import java.util.Arrays;
import java.util.Calendar;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component // 아래의 객체를 Spring 컨테이너의 빈으로 인식
@Aspect // 아래의 객체가 Aspect객체임을 알림
public class ExampleAdvice {
	
//	@Before("execution(* com.miniproject.service.board.BoardServiceImpl.saveNewBoard(..))")
//	public void startAOP(JoinPoint jp) {
//		System.out.println("--------------------------------------------------------");
//		System.out.println(" Start AOP!! ");
//		System.out.println("--------------------------------------------------------");
//		
//		System.out.println(Arrays.toString(jp.getArgs()));
//		
//	}
	
	@Around("execution(* com.miniproject.service.board.BoardServiceImpl.getEntireBoard(..))")
	public Object executionTimeLog(ProceedingJoinPoint pjp) throws Throwable {
		System.out.println("--------------------------------------------------------");
		System.out.println(" Start executionTimeLog AOP!! ");
		System.out.println("--------------------------------------------------------");
		
		long startTime = Calendar.getInstance().getTimeInMillis();
		System.out.println("게시글 등록 시작 시간 : " + startTime);
		
		Object returnValue = pjp.proceed();
		System.out.println(returnValue);
		
		System.out.println("--------------------------------------------------------");
		System.out.println(" target 실행 후! ");
		System.out.println("--------------------------------------------------------");
		
		long endTime = Calendar.getInstance().getTimeInMillis();
		System.out.println("게시글 등록 끝 시간 : " + endTime);
		
		System.out.println("게시글 등록 수행 시간 : " + (endTime - startTime));
		
		return returnValue;
	}
}
