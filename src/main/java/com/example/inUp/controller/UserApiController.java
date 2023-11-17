package com.example.inUp.controller;

import com.example.inUp.config.jwt.TokenProvider;
import com.example.inUp.domain.Users;
import com.example.inUp.domain.enums.Authority;
import com.example.inUp.dto.LoginTokenResponse;
import com.example.inUp.dto.UserRequestDto;
import com.example.inUp.dto.AddUserResponse;
import com.example.inUp.dto.ResponseDTO;
import com.example.inUp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserApiController {

  private final UserService userService;

  // Bean으로 작성해도 됨.
  private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  @Autowired
  TokenProvider tokenProvider;

  @PostMapping("/signup")
  public ResponseEntity<?> signup(@RequestBody UserRequestDto request) {
    try {
      Users userdata = Users.builder()
          .email(request.getEmail())
          .password(passwordEncoder.encode(request.getPassword()))
          .authority(Authority.ROLE_USER)
          .build();
      System.out.println(userdata);

      // 서비스를 이용해 리파지토리에 유저 저장
      Users registeredUser = userService.createUser(userdata);

      AddUserResponse addUserResponse = AddUserResponse.builder()
          .email(registeredUser.getEmail())
          .password(registeredUser.getPassword())
          .info("회원가입 성공")
          .build();
      // 유저 정보는 항상 하나이므로 그냥 리스트로 만들어야하는 ResponseDTO를 사용하지 않고 그냥 UserDTO 리턴.
      return ResponseEntity.ok(addUserResponse);

    } catch (Exception e) {

      // 예외가 나는 경우 bad 리스폰스 리턴.
      ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
      return ResponseEntity.badRequest().body(responseDTO);
    }
  }

  @PostMapping("/signin")
  public ResponseEntity<?> signin(@RequestBody UserRequestDto request) {
    Users userdata = userService.getByCredentials(request.getEmail(), request.getPassword(), passwordEncoder);

    //userdata 값이 null 이 아니면토큰 생성
    if (userdata != null) {
      final LoginTokenResponse userLoginInfo = userService.createToken(userdata);
      userLoginInfo.setAuth(userdata.getAuthority().toString());
      return ResponseEntity.ok().body(userLoginInfo);
    } else {
      ResponseDTO responseDTO = ResponseDTO.builder()
          .error("Login faild")
          .build();
      return ResponseEntity.badRequest().body(responseDTO);
    }
  }

  @PostMapping("/logout")
  public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
    new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
    return ResponseEntity.ok().body("logout");
  }

  @GetMapping("/loginInfo")
  @ResponseBody
  public String currentUserName(Principal principal) {
    return principal.getName();
  }
}




