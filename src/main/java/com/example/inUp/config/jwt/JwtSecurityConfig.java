package com.example.inUp.config.jwt;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

  private TokenProvider tokenProvider;

  public JwtSecurityConfig(TokenProvider tokenProvider){
    this.tokenProvider = tokenProvider;
  }

  //security로직에 JwtFilter를 등록함
  @Override
  public void configure(HttpSecurity http){
    JwtFilter customerFilter = new JwtFilter(tokenProvider);
    http.addFilterBefore(customerFilter, UsernamePasswordAuthenticationFilter.class);
  }
}