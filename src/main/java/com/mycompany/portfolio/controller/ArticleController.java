package com.mycompany.portfolio.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mycompany.portfolio.dto.ArticleDto;
import com.mycompany.portfolio.service.ArticleService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/article")
public class ArticleController {

	@Autowired
	private ArticleService articleService;
	
	@Value("${spring.servlet.multipart.location}")
	private String uploadDir;
	
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
		if(mf.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");
		}
		try {
			//날짜 기준 폴더명 지정
			String today = LocalDate.now().toString();
			Path folderPath = Paths.get(uploadDir, today);
			
			//폴더 존재 여부에 따라 생성 여부 결정
			if(!Files.exists(folderPath)) {
				Files.createDirectories(folderPath);
			}
			
			//
			String fileName = UUID.randomUUID().toString() + "_" + mf.getOriginalFilename();
			Path filePath = folderPath.resolve(fileName);
			
			Files.copy(mf.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
			
			String fileDownloadUrl = "/api/article/download_editor_image/" + today + "/" +fileName;
			
			return ResponseEntity.ok(fileDownloadUrl);
		} catch(IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not upload file");
		}
	}
	
	//Editor 이미지 다운로드
	@GetMapping("/download_editor_image/{date}/{fileName}")
	public ResponseEntity<?> downloadEditorImage(HttpServletResponse response, @PathVariable String date, @PathVariable String fileName) {
		try {
			Path filePath = Paths.get(uploadDir).resolve(date).resolve(fileName).normalize();
			Resource rsc = new UrlResource(filePath.toUri());
			if(rsc.exists()) {
				return ResponseEntity.ok()
						.contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
						.body(rsc);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
		} catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	//Editor 이미지 삭제
	@DeleteMapping("delete_editor_image/{date}/{fileName}")
	public ResponseEntity<?> deleteEditorImage(@PathVariable String date, @PathVariable String fileName) {
		try {
			Path filePath = Paths.get(uploadDir).resolve(date).resolve(fileName).normalize();
			
			if(Files.exists(filePath)) {
				Files.delete(filePath);
				return ResponseEntity.ok("File delete successfully");
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File Not Found");
			}
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could Not Delete File");
		}
	}
}
