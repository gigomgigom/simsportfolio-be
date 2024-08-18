package com.mycompany.portfolio.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mycompany.portfolio.dto.ArticleDto;
import com.mycompany.portfolio.dto.ArticleImageDto;
import com.mycompany.portfolio.service.ArticleService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/article")
public class ArticleController {

	@Autowired
	private ArticleService articleService;
	
	//태그 리스트 응답
	@GetMapping("/get_tag_list")
	public ResponseEntity<?> getTagList() {
		
		return ResponseEntity.ok(null);
	}
	
	//목록 조회하기
	@GetMapping("/get_article_list")
	public ResponseEntity<?> getArticleList() {
		
		return ResponseEntity.ok(null);
	}
	
	//게시글 상세 조회
	@GetMapping("/get_article_detail/{articleId}")
	public ResponseEntity<?> getArticleDetail(@PathVariable int articleId) {
		
		return ResponseEntity.ok(null);
	}
	
	//게시글 작성
	@PostMapping("/write_article")
	public ResponseEntity<?> writeArticle(ArticleDto articleDto) {
		if(articleDto.getArticleImg() != null && articleDto.getArticleImg().isEmpty()) {
			MultipartFile mf = articleDto.getArticleImg();
			articleDto.setArticleImgOname(mf.getOriginalFilename());
			articleDto.setArticleImgType(mf.getContentType());
			try {
				articleDto.setArticleImgData(mf.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		int createdRow = articleService.writeArticle(articleDto);
		Map<String, Object> result = new HashMap<>();
		result.put("createdRow", createdRow);
		return ResponseEntity.ok(result);
	}
	
	//게시글 썸네일 이미지 다운로드
	@GetMapping("/download_article_thumbnail")
	public void downloadArticleThumbnail(HttpServletResponse response, int articleId) {
		ArticleDto article = articleService.getArticleThumbnail(articleId);
		if(article != null) {
			try {
				String fileName = new String(article.getArticleImgOname().getBytes("UTF-8"), "ISO-8859-1");
				response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		response.setContentType(article.getArticleImgType());
		
		OutputStream os;
		try {
			os = response.getOutputStream();
			os.write(article.getArticleImgData());
			os.flush();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Editor 이미지 업로드
	@PostMapping("/upload_editor_image")
	public ResponseEntity<?> uploadEditorImage(@RequestParam("file") MultipartFile mf) {
		ArticleImageDto articleImage = new ArticleImageDto();
		articleImage.setImageOName(mf.getOriginalFilename());
		articleImage.setImageType(mf.getContentType());
		try {
			articleImage.setImageData(mf.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		int imgId = articleService.addArticleContentImage(articleImage);
		String downloadUrl = "/api/article/download_editor_image?imageId="+imgId;
		return ResponseEntity.ok(downloadUrl);
	}
	
	//Editor 이미지 다운로드
	@GetMapping("/download_editor_image")
	public void downloadEditorImage(HttpServletResponse response, int imageId) {
		ArticleImageDto articleImage = articleService.getArticleContentImage(imageId);
		if(articleImage != null) {
			try {
				String fileName = new String(articleImage.getImageOName().getBytes("UTF-8"), "ISO-8859-1");
				response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			response.setContentType(articleImage.getImageType());
			OutputStream os;
			try {
				os = response.getOutputStream();
				os.write(articleImage.getImageData());
				os.flush();
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
