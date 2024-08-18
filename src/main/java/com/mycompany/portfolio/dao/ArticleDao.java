package com.mycompany.portfolio.dao;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.portfolio.dto.ArticleDto;

@Mapper
public interface ArticleDao {
	//게시글 작성
	public int insertArticle(ArticleDto articleDto);
	//게시글의 썸네일 이미지 정보만 가져오기
	public ArticleDto selectArticleHasThumbnail(int articleId);
	
}
