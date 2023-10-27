package com.example.inUp.controller;

import com.example.inUp.dto.AddUserRequest;
import com.example.inUp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class UserApiController {
  private final UserService userService;

  @PostMapping("/user")
  public String sginup(AddUserRequest request){
    userService.save(request);
    return "Success";
  }
}
