package com.mycompany.portfolio.dao;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.portfolio.dto.ArticleImageDto;

@Mapper
public interface ArticleImgDao {
	
	public void updateArticleImg(int articleId, int imgId);

	public int insertArticleContentImg(ArticleImageDto articleImage);

	public ArticleImageDto selectArticleContentImg(int imageId);
}
