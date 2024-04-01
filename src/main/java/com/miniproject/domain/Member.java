package com.miniproject.domain;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.Data;

@Data
public class Member {
	private String userId;
	private String userPwd;
	private String userEmail;
	private Date registerDate;
	private int userImg;
	private int userPoint;
	private String memberImg;
	private String isAdmin;
	private Timestamp sessionLimit;
	private String sessionKey;
}
