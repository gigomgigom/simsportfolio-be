package com.mycompany.portfolio.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleTagDao {
	//게시글의 태그 정보를 저장하기
	public void insertArticleTag(int articleId, int tagId);

}
