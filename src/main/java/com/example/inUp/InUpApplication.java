package com.example.inUp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class InUpApplication {
  public static void main(String[] args) {
    SpringApplication.run(InUpApplication.class, args);
  }

}
