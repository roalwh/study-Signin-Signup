package com.example.inUp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserViewController {

  @GetMapping("/login")
  public String Login(){
    return "Login";
  }

  @GetMapping("/signup")
  public String signup(){
    return "signup";
  }

  @GetMapping("/main")
  public String mainpage(){
    return "mainpage";
  }
}
