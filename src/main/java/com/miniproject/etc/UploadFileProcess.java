package com.miniproject.etc;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Mode;
import org.springframework.util.FileCopyUtils;

import com.miniproject.domain.UploadedFile;

public class UploadFileProcess {
	
	public static UploadedFile fileUpload(String originalFileName, long size, String contentType, byte[] data, String realPath) throws IOException {
		
		String completePath = makeCalculatePath(realPath); // 물리적경로 + \년\월\일
		
		UploadedFile uf = new UploadedFile();
		
		if (size > 0) {
			uf.setNewFileName(getNewFileName(originalFileName, realPath, completePath));
			uf.setOriginalFileName(originalFileName);
			uf.setSize(size);
			uf.setExt(originalFileName.substring(originalFileName.lastIndexOf(".")));
			
			
			FileCopyUtils.copy(data, new File(realPath + uf.getNewFileName())); // 원본이미지 저장
			
			if (ImgMimeType.contentTypeIsImage(contentType)) {
				// 파일이 이미지인 경우 - > 썸네일 생성
				System.out.println("이미지입니다.....");
				
				makeThumbNailImage(uf, completePath, realPath);
				
			} else {
				System.out.println("이미지가 아닙니다...");
			}
			
		}
		return uf;
		
	}

	private static void makeThumbNailImage(UploadedFile uf, String completePath, String realPath) throws IOException {
		// 저장된 원본 파일을 읽어서 스케일 다운하여 썸네일을 만든다.
		System.out.println("thumbNail이미지 만들기 : " + realPath + uf.getNewFileName());
		
		BufferedImage originImg = ImageIO.read(new File(realPath + uf.getNewFileName()));
		
		
//		Scalr.resize(originImg, Scalr.Method.AUTOMATIC, Mode.FIT_TO_HEIGHT, 50);
		BufferedImage thumbNailImg = Scalr.resize(originImg, Mode.FIT_TO_HEIGHT, 50);
		
		// 썸네일 저장
		String thumbImgName = "thumb_" + uf.getOriginalFileName();
		String ext = uf.getOriginalFileName().substring(uf.getOriginalFileName().lastIndexOf(".") + 1);
		File saveTarget = new File(completePath + File.separator + thumbImgName);
		
		
		if (ImageIO.write(thumbNailImg, ext, saveTarget)) { // 썸네일 저장
			uf.setThumbFileName(completePath.substring(realPath.length()) + File.separator + thumbImgName);
			System.out.println("썸네일 이미지 저장 완료");
		} else {
			System.out.println("썸네일 이미지 저장 실패");
		}
		
		
	}

	private static String getNewFileName(String originalFileName, String realPath, String completePath) {
		String uuid = UUID.randomUUID().toString();
		
		String newFileName = uuid + "_" + originalFileName;
		
		// 테이블에 저장될 업로드 파일이름
		System.out.println("테이블에 저장될 업로드 파일이름 : " + completePath.substring(realPath.length()) + File.separator + newFileName);
		
				
				
		
		return completePath.substring(realPath.length()) + File.separator + newFileName;
	}

	public static String makeCalculatePath(String realPath) {
		// 현재 날짜를 얻어오기
		Calendar cal = Calendar.getInstance();
		// 2024 / 01 / 22
//		String year = cal.get(Calendar.YEAR) + ""; // 인트를 스트링으로
//		String month = (cal.get(Calendar.MONTH) + 1) + "";
//		System.out.println(new DecimalFormat("00").format(cal.get(Calendar.MONTH) + 1));
//		
//		String date = cal.get(Calendar.DATE) + "";
//		System.out.println(year + ", " + month + ", " + date );
		
		// realPath\2024\01\22
		String year = File.separator + (cal.get(Calendar.YEAR) + ""); // \2024
		String month = year + File.separator + new DecimalFormat("00").format(cal.get(Calendar.MONTH) + 1); // \2024\01
		String date = month + File.separator + new DecimalFormat("00").format(cal.get(Calendar.DATE)); // \2024\01\22
		
		makeDirectory(realPath, year, month, date);
		
		return realPath + date;
		
	}

	private static void makeDirectory(String realPath, String...strings) { // 실질적으로 디렉토리를 만드는 메서드
		// year,month,date를 가변인자로 받아보자 (String...strings)
		// 디렉토리 생성
		 if(!new File(realPath + strings[strings.length -1]).exists()) { // date경로 디렉토리 없으면 (index번호로 따지면 0,1,2 인데 length로 따지면 3이니 -1을 해줘야한다. 
			 for (String path : strings) {
				 File tmp = new File(realPath + path);
				 if (!tmp.exists()) {
					 tmp.mkdir();
				 }
			 }
		 }
		
		 
	}

	/**
	 * @MethodName : deleteFile
	 * @author : jkw
	 * @date : 2024. 1. 23.
	 * @description :fileList에서 remFile파일과 thumbnailimage를 삭제		
	 * @param fileList
	 * @param remFile
	 * @param realPath
	 */
	public static void deleteFile(List<UploadedFile> fileList, String remFile, String realPath) {
		// (x)를 클릭 -> 삭제
		
		for (UploadedFile uf : fileList) {
			if (remFile.equals(uf.getOriginalFileName())) { // 지워야할 파일 찾았다.
				System.out.println("remFile: " + remFile);
				
				File delFile = new File(realPath + uf.getNewFileName());
				
				if(delFile.exists()) {
					delFile.delete();
				}
				
				if (uf.getThumbFileName() != null) { // 이미지 파일이면
					File thumbFile = new File(realPath + uf.getThumbFileName());
					
					if (thumbFile.exists()) {
						thumbFile.delete();
					}
				}
				
			}
		}
		
		
		
	}

	public static void deleteAllFile(List<UploadedFile> fileList, String realPath) {
		// 취소버튼 클릭시 모든 드래그드랍한 파일 삭제
		
		for (UploadedFile uf : fileList) {
			
				
				File delFile = new File(realPath + uf.getNewFileName());
//				System.out.println("delFile " + delFile);
				
				if(delFile.exists()) {
					delFile.delete();	
				}
				
				if (uf.getThumbFileName() != null) {
					File thumbFile = new File(realPath + uf.getThumbFileName());
//					System.out.println("uf.getThumbFileName " + uf.getThumbFileName());
					
					if (thumbFile.exists()) {
						thumbFile.delete();
					}
				}
				
			
		}
		
	}
	
}
