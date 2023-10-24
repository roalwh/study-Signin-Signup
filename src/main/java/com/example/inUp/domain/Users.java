package com.example.inUp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;



@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@Getter
@NonNull
@Entity
public class Users implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "uid", updatable = false)
  @Comment("회원 번호")
  private Long uid;

  @NotNull
  @Column(name = "email", unique = true)
  @Comment("회원 이메일")
  private String email;

  @Column(name = "password")
  @Comment("비밀번호")
  private String password;

  @Builder
  public Users(String email,String password, String auth){
    this.email = email;
    this.password = password;
  }

  @Override // 권한 반환
  public Collection<? extends GrantedAuthority> getAuthorities(){
    return List.of(new SimpleGrantedAuthority("user"));
  }

  @Override
  public String getUsername(){
    //사용자의 이메일 반환
    return email;
  }
  @Override
  public String getPassword(){
    //사용자의 패스워드 반환
    return password;
  }
  @Override
  public boolean isAccountNonExpired(){
    //사용자의 계정 만료
    return true;
  }

  @Override
  public boolean isAccountNonLocked(){
    // 계정 잠금 여부 반환
    //true 정상, false 잠금
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired(){
    //패스워드 만료 여부 반환
    //true 정상,false 교체 필요
    return true;
  }

  @Override
  public boolean isEnabled(){
    //계정 사용 가능 여부 반환
    return true;
  }
}
