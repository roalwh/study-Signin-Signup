package com.example.inUp.controller;

import com.example.inUp.domain.User;
import com.example.inUp.dto.AddUserRequest;
import com.example.inUp.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("회원가입 테스트")
@SpringBootTest
class UserApiControllerTest {

  @Autowired
  UserRepository userRepository;
  @Test
  void SginUpTest(){
    User Userdate = User.builder().email("test1@test.com").password("test123").build();
    userRepository.save(Userdate);
  }




}