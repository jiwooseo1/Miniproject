package com.miniproject.etc;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;

public class ImgMimeType {
	
	private static Map<String, String> imgMimeType;
	
	{
		// instance 멤버 변수 초기화 블럭
		
	}
	
	static {
		// static 변수의 초기화 블럭
		imgMimeType = new HashMap<String, String>();
		
		imgMimeType.put("jpg", MediaType.IMAGE_JPEG.toString());
		imgMimeType.put("jpeg", MediaType.IMAGE_JPEG.toString());
		imgMimeType.put("gif", MediaType.IMAGE_GIF_VALUE);
		imgMimeType.put("png", MediaType.IMAGE_PNG_VALUE);
	
	}
	
	public static boolean extIsImage(String ext) {
		return imgMimeType.containsKey(ext);
	}
	
	public static boolean contentTypeIsImage(String contentType) {
		return imgMimeType.containsValue(contentType);
	}
	
}
