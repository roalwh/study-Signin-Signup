package com.example.inUp.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Slf4j
@Component
public class JwtFilter extends GenericFilterBean{
  public static final String AUTHORIZATION_HEADER = "Authorization";

  private TokenProvider tokenProvider;

  public JwtFilter(TokenProvider tokenProvider){
    this.tokenProvider = tokenProvider;
  }
  //토큰의 인증정보를 securityContext에 저장하는 역할 수행
  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                       FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
    //resolveToken을 통해 request에서 jwt 토큰을 획득
    String jwt = resolveToken(httpServletRequest);
    String requestURI = httpServletRequest.getRequestURI();

    //validateToken 을 통해 토큰의 유효성 검사, 정상이면 토큰을 Security Context에 저장
    if(StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)){
      Authentication authentication = tokenProvider.getAuthentication(jwt);
      SecurityContextHolder.getContext().setAuthentication(authentication);
      log.debug("Security Context에 '{}' 인증정보를 저장했습니다. uri:{}",authentication.getName(), requestURI);
    }else{
      log.debug("유효한 JWT 토큰이 없습니다.",requestURI);
    }
    filterChain.doFilter(servletRequest, servletResponse);
  }

  //request Header 에서 토큰정보를 읽어옴.
  private String resolveToken(HttpServletRequest request){
    String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
    if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
      return bearerToken.substring(7);
    }
    return null;
  }

}
