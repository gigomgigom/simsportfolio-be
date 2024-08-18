package com.mycompany.portfolio.dto;

import lombok.Data;

@Data
public class ArticleImageDto {
	private int imageId;
	private String imageOName;
	private String imageType;
	private byte[] imageData;
	private int articleId;
}
