package com.drizh2.instazoo.payload.request;

import com.drizh2.instazoo.annotations.PasswordMatches;
import com.drizh2.instazoo.annotations.ValidEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@PasswordMatches
public class SignupRequest {

    @Email
    @NotBlank(message = "User email is required!")
    @ValidEmail
    private String email;
    @NotEmpty(message = "Please enter your first name!")
    private String firstname;
    @NotEmpty(message = "Please enter your last name!")
    private String lastname;
    @NotEmpty(message = "Please enter your username!")
    private String username;
    @NotEmpty(message = "Please enter your password!")
    @Size(min = 6)

    private String password;
    private String confirmPassword;
}
