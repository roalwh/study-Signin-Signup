package com.example.inUp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUserRequest {
  @NotNull
  @Size(min=3, max=50)
  private String email;

  @NotNull
  @Size(min=3, max=100)
  private String password;
}
