package com.example.inUp.config;

import com.example.inUp.config.jwt.JwtAccessDeniedHandler;
import com.example.inUp.config.jwt.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.CorsFilter;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@EnableWebSecurity // 기본적인 웹보안을 활성화하겠다
@EnableMethodSecurity // @PreAuthorize 어노테이션 사용을 위해 선언
public class WebSecurityConfig {

  @Autowired
  CorsFilter corsFilter;
  @Autowired
  JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  @Autowired
  JwtAccessDeniedHandler jwtAccessDeniedHandler;

//h2 설정
  @Bean
  public WebSecurityCustomizer configure() {
    return (web) -> web.ignoring()
        .requestMatchers(toH2Console())
        .requestMatchers(new AntPathRequestMatcher("/static/**"));
  }
  //http 요청에 대한 인가 설정
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
    http.addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class);

    http.exceptionHandling(exception -> exception
        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
        .accessDeniedHandler(jwtAccessDeniedHandler));

    http.sessionManagement(session -> session
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    //인증, 인가설정
    //인가할 권한범위 permitAll() 전체 ,hasAuthority("ROLE_ADMIN") 등을 이용한 권한설정
    //.requestMatchers("/**").permitAll() //모든 사이트일 경우 /**
    //`antMatchers("/admin/")`**는 "/admin/"으로 시작하는 모든 URL에 대한 접근 권한 설정
    //`requestMatchers(HttpMethod.GET, "/public/")`**는 HTTP GET 요청 중 "/public/"으로 시작하는 URL에 대한 보안 설정
    http.authorizeHttpRequests((authorize) -> authorize
        .requestMatchers(
            new AntPathRequestMatcher("/login"),
            new AntPathRequestMatcher("/signup"),
            new AntPathRequestMatcher("/user"),
            new AntPathRequestMatcher("/api/**")
        ).permitAll()
        .anyRequest().authenticated()
        );

    //http.csrf(AbstractHttpConfigurer::disable);
    http.csrf(AbstractHttpConfigurer::disable);
    return http.build();
  }
}
