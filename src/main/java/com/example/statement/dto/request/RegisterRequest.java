package com.example.statement.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegisterRequest {

    private  @NotBlank String userName;
    private  @NotBlank String confirmPassword;
    private  @NotBlank String password;
    private  @NotBlank @Email String email;
}
