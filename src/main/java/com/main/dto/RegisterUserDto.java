package com.main.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class RegisterUserDto {
  @NotBlank @Size(min=3,max=30)
  private String username;
  @NotBlank @Size(min=6,max=100)
  private String password;
  @NotBlank
  private String fullName;
  @NotBlank @Email
  private String email;

  // getters/setters
}
