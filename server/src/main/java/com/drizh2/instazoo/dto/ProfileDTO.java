package com.drizh2.instazoo.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ProfileDTO {

    private Long id;
    @NotEmpty
    private String firstname;
    @NotEmpty
    private String lastname;
    @NotEmpty
    private String username;
    private String bio;

}
