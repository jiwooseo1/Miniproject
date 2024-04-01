package com.miniproject.domain;

import lombok.Data;

@Data
public class UploadedFile {
	private String originalFileName;
	private String ext;
	private String newFileName;
	private long size;
	private int boardNo;
	private String base64String;
	private String thumbFileName;
}
