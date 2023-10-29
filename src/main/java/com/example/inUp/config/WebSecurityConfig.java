package com.example.inUp.config;

import com.example.inUp.config.jwt.JwtAccessDeniedHandler;
import com.example.inUp.config.jwt.JwtAuthenticationEntryPoint;
import com.example.inUp.config.jwt.JwtSecurityConfig;
import com.example.inUp.config.jwt.TokenProvider;
import com.example.inUp.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {

  private final UserDetailService userDetailService;
  private final TokenProvider tokenProvider;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

  @Bean
  public WebSecurityCustomizer configure() {
    return (web) -> web.ignoring()
        .requestMatchers(toH2Console())
        .requestMatchers(new AntPathRequestMatcher("/static/**"));
  }
  //http 요청에 대한 인가 설정
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

    //인증, 인가설정
    //인가할 권한범위 permitAll() 전체 ,hasAuthority("ROLE_ADMIN") 등을 이용한 권한설정
    //.requestMatchers("/**").permitAll() //모든 사이트일 경우 /**
    //`antMatchers("/admin/")`**는 "/admin/"으로 시작하는 모든 URL에 대한 접근 권한 설정
    //`requestMatchers(HttpMethod.GET, "/public/")`**는 HTTP GET 요청 중 "/public/"으로 시작하는 URL에 대한 보안 설정
    http.authorizeHttpRequests((authorize) -> authorize
        .requestMatchers(
            new AntPathRequestMatcher("/login"),
            new AntPathRequestMatcher("/signup"),
            new AntPathRequestMatcher("/user")
        ).permitAll()
        .anyRequest().authenticated()
        )
    // 로그인 설정
    .formLogin((login) -> login
        //로그인 요청
        .loginPage("/login")
        //로그인 성공시 리다이렉션
        .defaultSuccessUrl("/main"));

    //로그아웃 설정
    http.logout((logout) -> logout
        //로그아웃 성공시 리다이렉션
        .logoutSuccessUrl("/login")
        //로그아웃 성공 후 세션 삭제
        .invalidateHttpSession(true));

    //http.csrf(AbstractHttpConfigurer::disable);
    http.csrf((csrf) -> csrf.disable());
    http.exceptionHandling(exception -> exception
        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
        .accessDeniedHandler(jwtAccessDeniedHandler));
    http.apply(new JwtSecurityConfig(tokenProvider));
    return http.build();
  }

  //AuthenticationManager 설명
  //https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/index.html#customize-global-authentication-manager
  //https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/dao-authentication-provider.html

  //DaoAuthenticationProvider 설명
  //https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/dao-authentication-provider.html#page-title
  @Bean
  public DaoAuthenticationProvider authenticationManager(BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailService userDetailService)
    throws Exception{
    DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
    daoAuthenticationProvider.setUserDetailsService(userDetailService); //사용자 정보 서비스 설정
    daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());

    return daoAuthenticationProvider;
  }

  //패스워드 인코더
  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder(){
    return new BCryptPasswordEncoder();
  }

}
