package com.yashg.rideshare.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 30, message = "Username must be 3–30 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 50, message = "Password must be 6–50 characters")
    private String password;

    @NotBlank(message = "Role is required")
    @Pattern(regexp = "ROLE_USER|ROLE_DRIVER", message = "Role must be ROLE_USER or ROLE_DRIVER")
    private String role;
}
