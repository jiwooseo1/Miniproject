package com.miniproject.persistence;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.miniproject.domain.PointLog;

@Repository
public class PointLogDAOImpl implements PointLogDAO{
	
   @Inject
   private SqlSession ses;

   private static String ns = "com.miniproject.mappers.pointLogMapper";
   
	@Override
	public int insertPointLog(PointLog pointLog) {
		
		return ses.insert(ns + ".insertPointLog", pointLog);
	}

}
