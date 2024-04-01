package com.miniproject.etc;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.miniproject.domain.PointLog;
import com.miniproject.persistence.PointLogDAO;

@Component
public class SampleScheduler {
	
	@Autowired
	private PointLogDAO pdao;
	
//	@Scheduled(cron="0 17 10 * * *") // 매일 10시 17분에
//	@Scheduled(cron="* * * * * *") // 매 초마다 
//	@Scheduled(cron="0 0/1 * * * *") //  
	public void sampleSchedule() throws Exception {
		System.out.println("=============================Scheduling============================");
		pdao.insertPointLog(new PointLog(-1, null, "테스트", 1, "abcd"));
		
	}
	
	
}
