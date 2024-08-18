package com.mycompany.portfolio.dto;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ArticleDto {
	private int articleId;
	private String articleTitle;
	private Date articleDate;
	private int articleHitCnt;
	private String articleImgOname;
	private String articleImgType;
	private byte[] articleImgData;
	private String articleOutline;
	private String articleContent;
	
	private MultipartFile articleImg;
	
	private int[] tagList;
	private int[] imgList;
}
