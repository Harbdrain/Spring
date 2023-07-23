package com.danil.spring.dto;

import com.danil.spring.model.User;
import com.danil.spring.model.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAllUsersResponse {
    private String username;
    private UserRole role;

    public static GetAllUsersResponse fromUser(User user) {
        return GetAllUsersResponse.builder()
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }
}
