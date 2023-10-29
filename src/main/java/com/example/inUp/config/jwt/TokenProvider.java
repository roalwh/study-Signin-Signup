package com.example.inUp.config.jwt;

import com.example.inUp.domain.user;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;


@Slf4j
@Component
public class TokenProvider implements InitializingBean {

  private final String issuer;
  private final String secret;
  private final long expiredSeconds;

  private SecretKey key;

  public TokenProvider(
      @Value("${jwt.issuer}")
      String issuer,
      @Value("${jwt.secret_key}")
      String secret,
      @Value("${jwt.expired_seconds}")
      long expiredSeconds
  ){
    this.issuer = issuer;
    this.secret = secret;
    this.expiredSeconds = expiredSeconds * 1000; //3600 * 1000 ms = 1시간
  }

  @Override
  public void afterPropertiesSet(){
    //TokenProvider bean 생성 후 생성자로 주입받은 secret 값을 이용해 암호화 키 생성
    byte[] keyBytes = Decoders.BASE64.decode(secret);
    this.key = Keys.hmacShaKeyFor(keyBytes);
  }


  //  토큰발급
  private String makeToken(Authentication authentication) {
    String authorities = authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(","));

    //토큰 유효시간 설정
    long now = (new Date()).getTime();
    Date validity = new Date(now + expiredSeconds*1000); //3600*1000ms

    return Jwts.builder()
        .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
        .setIssuer(issuer)
        .setExpiration(validity)
        .setSubject(authentication.getName())
        .claim("auth", authorities)
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }
  public Authentication getAuthentication(String token) {
    Claims claims = Jwts
        .parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody();

    Collection<? extends GrantedAuthority> authorities =
        Arrays.stream(claims.get("auth").toString().split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    User principal = new User(claims.getSubject(),"",authorities);
    return new UsernamePasswordAuthenticationToken(principal, token, authorities);
  }

  //토큰의 유효성 검사
  public boolean validateToken(String token){
    try{
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    }catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e){
      log.info("잘못된 JWT 서명입니다.");
    }catch (ExpiredJwtException e){
      log.info("만료된 JWT 서명입니다.");
    }catch (UnsupportedJwtException e){
      log.info("지원되지 않는 JWT 서명입니다.");
    }catch (IllegalArgumentException e){
      log.info("JWT 토큰이 잘못되었습니다.");
    }
    return false;
  }
}