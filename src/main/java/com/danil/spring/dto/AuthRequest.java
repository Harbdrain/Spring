package com.danil.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {
    private String username;

    private String password;

    public boolean valid() {
        return this.username != null && this.password != null
                && !this.username.isBlank() && !this.password.isBlank();
    }
}
