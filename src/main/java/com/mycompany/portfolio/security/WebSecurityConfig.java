package com.mycompany.portfolio.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class WebSecurityConfig {
	//인증 필터 체인을 관리 객체로 등록
		@Bean
		public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
			//Rest API에서 로그인 폼을 제공하지 않으므로 폼을 통한 로그인 인증을 하지 않도록 설정
			//로그인 폼은 front-end 에서 제공해야 함
			http.formLogin(config -> config.disable());
			
			//HttpSession을 사용하지 않도록 설정
			http.sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
			
			//사이트간 요청 위조 방지 비활성화(GET 방식 이외의 방식을 요청할 경우 _csrf 토큰 요구하기 때문)
			http.csrf(config -> config.disable());
			
			//CORS 설정(다른(크로스) 도메인에서 받은 인증 정보(AccessToken)로 요청할 경우 허가)
			http.cors(config -> {});
			
			return http.build();
		}
	//다른(크로스) 도메인 제한 설정
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		//요청 사이트 제한
		configuration.addAllowedOrigin("*");
		//요청 방식 제한
		configuration.addAllowedMethod("*");
		//요청 헤더 제한
		configuration.addAllowedHeader("*");
		//모든 URL에 대해 위 설정을 내용 적용
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}







