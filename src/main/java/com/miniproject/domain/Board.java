package com.miniproject.domain;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class Board {
		private int no;
		private String writer;
		private String title;
		private Timestamp postDate;
		private String content;
		private int readcount;
		private int likecount;
		private int ref;
		private int step;
		private int reforder;
		private String isDelete;
		
}
