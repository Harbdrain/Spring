package com.danil.spring.dto;

import com.danil.spring.model.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {
    private String username;
    private String password;
    private UserRole role;

    public boolean valid() {
        return this.username != null && this.password != null && this.role != null
                && !this.username.isBlank() && !this.password.isBlank();
    }
}
