package com.mycompany.portfolio.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleTagDao {

	public void insertArticleTag(int articleId, int tagId);

}
