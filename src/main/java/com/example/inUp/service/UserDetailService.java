package com.example.inUp.service;

import com.example.inUp.domain.Users;
import com.example.inUp.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {

  private final UserRepository userRepository;
  @Override
  @Transactional
  public UserDetails loadUserByUsername(final String email) {
    Users users = userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException(email + " -> 데이터베이스에서 찾을 수 없습니다."));

    if(!users.isEnabled()) throw new RuntimeException(users.isEnabled() + " -> 활성화되어 있지 않습니다.");
    return users;
  }

}
