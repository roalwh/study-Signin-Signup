package com.example.inUp.repository;


import com.example.inUp.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users,Long> {
  Optional<Users> findByEmail(String email);

  boolean existsByEmail(String email);
}
