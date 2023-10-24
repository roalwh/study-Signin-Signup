package com.example.inUp.repository;

import com.example.inUp.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {
  Optional<Users> findByEmail(String email);
}
