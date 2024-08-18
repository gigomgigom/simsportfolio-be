package com.mycompany.portfolio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.portfolio.dao.ArticleDao;
import com.mycompany.portfolio.dao.ArticleImgDao;
import com.mycompany.portfolio.dao.ArticleTagDao;
import com.mycompany.portfolio.dto.ArticleDto;
import com.mycompany.portfolio.dto.ArticleImageDto;

@Service
public class ArticleService {
	
	@Autowired
	ArticleDao articleDao;
	@Autowired
	ArticleTagDao articleTagDao;
	@Autowired
	ArticleImgDao articleImgDao;
	
	//게시글 조회
	//-------------------------------------------------------------------------
	//게시글 썸네일 이미지 정보 가져오기
	public ArticleDto getArticleThumbnail(int articleId) {
		ArticleDto acticle = articleDao.selectArticleHasThumbnail(articleId);
		return acticle;
	}
	
	//게시글 작성, 수정
	//-------------------------------------------------------------------------
	//게시글 작성
	@Transactional
	public int writeArticle(ArticleDto articleDto) {
		int createdArticleRow = articleDao.insertArticle(articleDto);
		if(articleDto.getTagList().length > 0) {
			for(int tagId : articleDto.getTagList()) {
				articleTagDao.insertArticleTag(articleDto.getArticleId(), tagId);
			}
		}
		if(articleDto.getImgList().length > 0) {
			for(int imgId : articleDto.getImgList()) {
				articleImgDao.updateArticleImg(articleDto.getArticleId(), imgId);
			}
		}
		return 0;
	}
	
	//게시글 컨텐츠 내부에 들어가는 이미지 파일 처리
	//--------------------------------------------------------------------
	//게시글 컨텐츠에 들어가는 이미지파일 정보 추가하기
	public int addArticleContentImage(ArticleImageDto articleImage) {
		int imgId = articleImgDao.insertArticleContentImg(articleImage);
		return imgId;
	}
	//게시글 컨텐츠에 들어가는 이미지파일 정보 가져오기
	public ArticleImageDto getArticleContentImage(int imageId) {
		ArticleImageDto articleImage = articleImgDao.selectArticleContentImg(imageId);
		return articleImage;
	}
}
