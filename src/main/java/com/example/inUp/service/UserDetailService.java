package com.example.inUp.service;

import com.example.inUp.domain.model.Users;
import com.example.inUp.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {

  private final UsersRepository usersRepository;

  @Override
  public Users loadUserByUsername(String email) {
    return usersRepository.findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException((email)));
  }
}
