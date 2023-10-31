package com.example.inUp.dto;

import com.example.inUp.domain.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AddUserResponse {
  private String email;
  private String password;
  private String info;
}
