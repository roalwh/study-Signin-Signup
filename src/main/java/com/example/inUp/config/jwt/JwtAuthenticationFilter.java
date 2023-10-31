package com.example.inUp.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  @Autowired
  private TokenProvider tokenProvider;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String requestURI = request.getRequestURI();
    try {
      // 요청에서 토큰 가져오기
      String token = parseBearerToken(request);
      log.info("Filter is running....");

      // 토큰 검사, JWT이므로 인가 서버에 요청하지 않고도 검증가능
      if (token != null && !token.equalsIgnoreCase("null")) {
        // userId 가져오기, 위조된경우 예외처리됨(tokenProvider.java 파일 참고)

        Authentication authentication = tokenProvider.validateAndGetUserinfo(token);
        log.info("Authenticated user ID : " + authentication.getName());

        // 인증완료; SecurityContextHolder에 등록되야 인증된 사용자로 처리함
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
      }
    } catch (Exception ex) {
      log.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
    }
    filterChain.doFilter(request, response);
  }

  private String parseBearerToken(HttpServletRequest request) {
    // Http 요청의 헤더를 파싱하여 Bearer 토큰을 리턴시킨다.
    String bearerToken = request.getHeader("Authorization");
    // hasText(bearerToken) 널같은 값체크, startsWith("Bearer ") 시작을 Bearer로 시작하는지
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      // substring(7)  bearerToken 토큰의 앞 7자리 리턴
      return bearerToken.substring(7);
    }
    return null;
  }

}