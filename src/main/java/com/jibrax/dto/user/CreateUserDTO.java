package com.jibrax.dto.user;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDTO {
    @NotBlank(message = "The user's firstname is mandatory")
    private String firstname;

    @NotBlank(message = "The user's lastname is mandatory")
    private String lastname;

    @NotBlank(message = "The username is mandatory")
    private String username;

    @NotBlank(message = "The password is mandatory")
    @Size(min = 8, message = "The password must contain 8 or more characters")
    private String password;

    @NotBlank(message = "The email is mandatory")
    @Email(message = "Email format is invalid")
    private String email;

    private Long teamId;

    private byte[] image;
}