package com.mycompany.portfolio.dao;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.portfolio.dto.ArticleImageDto;

@Mapper
public interface ArticleImgDao {
	//게시글 내용에 들어가는 이미지에 생성된 게시글 번호 등록하기
	public void updateArticleImg(int articleId, int imgId);
	//게시글 내용에 들어가는 이미지 저장
	public int insertArticleContentImg(ArticleImageDto articleImage);
	//게시글 내용에 들어가는 이미지 정보 가져오기
	public ArticleImageDto selectArticleContentImg(int imageId);
}
