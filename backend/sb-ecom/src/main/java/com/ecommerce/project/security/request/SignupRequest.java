package com.ecommerce.project.security.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.Data;

@Data
public class SignupRequest {

  @NotBlank
  @Size(min = 3, max = 20, message = "username should not less than 3 characters")
  private String username;

  @NotBlank
  @Size(max = 50, message = "please enter valid email")
  @Email
  private String email;

  @NotBlank
  @Size(min = 6)
  private String password;

  private Set<String> roles;
}
