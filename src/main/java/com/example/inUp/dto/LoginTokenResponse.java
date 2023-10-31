package com.example.inUp.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginTokenResponse {
  private String email;
  private String token;
}
