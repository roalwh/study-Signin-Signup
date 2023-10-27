package com.example.inUp.service;

import com.example.inUp.domain.User;
import com.example.inUp.dto.AddUserRequest;
import com.example.inUp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
  private final UserRepository userRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  //회원 가입
  //회원 정보 저장 후 Uid 값 리턴
  public Long save(AddUserRequest addUserRequest){
    return userRepository.save(User.builder()
        .email(addUserRequest.getEmail())
        .password(bCryptPasswordEncoder.encode(addUserRequest.getPassword()))
        .build()).getUid();
  }

}
