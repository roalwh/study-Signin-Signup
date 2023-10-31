package com.example.inUp.service;

import com.example.inUp.config.jwt.TokenProvider;
import com.example.inUp.domain.Users;
import com.example.inUp.dto.LoginTokenResponse;
import com.example.inUp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
  private final UserRepository userRepository;

  @Autowired
  TokenProvider tokenProvider;

  //회원 가입
  public Users createUser(final Users userdata) {
    if(userdata == null || userdata.getEmail() ==null){
      throw new RuntimeException("Invalid arguments");
    }
    if(userRepository.existsByEmail(userdata.getEmail())){
      log.warn("Email already exists {}", userdata.getEmail());
      throw new RuntimeException("Email already exists");
    }
    return userRepository.save(userdata);
  }

  public Users getByCredentials(String email, String password, PasswordEncoder passwordEncoder) {
    final Users UserOrigina = userRepository.findByEmail(email).get();
    //패스워드 비교
    if(UserOrigina !=null && passwordEncoder.matches(password,UserOrigina.getPassword())){
      return UserOrigina;
    }
    return null;
  }

  public LoginTokenResponse createToken(Users userdata) {

    // 인증 정보를 기준으로 jwt access 토큰 생성
    String accessToken = tokenProvider.createToken(userdata);

    return LoginTokenResponse.builder()
        .email(userdata.getEmail())
        .token(accessToken)
        .build();
  }
}

