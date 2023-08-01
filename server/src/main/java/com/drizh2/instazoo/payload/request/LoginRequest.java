package com.drizh2.instazoo.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginRequest {

    @NotNull(message = "You entered wrong username!")
    private String username;
    @NotNull(message = "You entered wrong password!")
    private String password;
}
