package com.example.inUp.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Getter
@Setter
@ConfigurationProperties(value = "jwt")
@Component
public class JwtProperties {
  private String header;
  private String issuer;;
  private String secretKey;
  private Long expiredSeconds;
}
