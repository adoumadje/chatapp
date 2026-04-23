package com.adoumadje.chatapp.user.dto;

import com.adoumadje.chatapp.user.validation.PasswordMatch;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
@PasswordMatch
public class UserRegistrationDto {
    @NotBlank(message = "Username is mandatory")
    private String username;
    @NotBlank(message = "Firstname is mandatory")
    private String firstname;
    @NotBlank(message = "Lastname is mandatory")
    private String lastname;
    @NotNull(message = "email is mandatory")
    private String email;
    private String profilePictureUrl;
    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).+$",
            message = "password must contain at least 1 Uppercase, 1 digit and 1 special character")
    private String password;
    @NotBlank(message = "confirmPassword is mandatory")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).+$",
            message = "password must contain at least 1 Uppercase, 1 digit and 1 special character")
    private String confirmPassword;
}
