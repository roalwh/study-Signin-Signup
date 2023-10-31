package com.example.inUp.config.jwt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JwtPropertiesTest {

  @Autowired
  JwtProperties jwtProperties;

  @DisplayName("yml값 불러오기")
  @Test
  void test(){
    System.out.println("============================");
    System.out.println(jwtProperties.getHeader());
    System.out.println(jwtProperties.getIssuer());
    System.out.println(jwtProperties.getSecretKey());
    System.out.println(jwtProperties.getExpiredSeconds());
    System.out.println("============================");
  }
}