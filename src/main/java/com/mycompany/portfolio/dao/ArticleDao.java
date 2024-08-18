package com.mycompany.portfolio.dao;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.portfolio.dto.ArticleDto;

@Mapper
public interface ArticleDao {

	public int insertArticle(ArticleDto articleDto);

	public ArticleDto selectArticleHasThumbnail(int articleId);
	
}
