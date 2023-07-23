package com.danil.spring.dto;

import java.util.ArrayList;
import java.util.List;

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
public class GetUserResponse {
    private String username;
    private UserRole role;
    private List<String> files;

    public static GetUserResponse fromUser(User user) {
        GetUserResponse response = GetUserResponse.builder()
                .username(user.getUsername())
                .role(user.getRole())
                .files(new ArrayList<>())
                .build();
        user.getEvents().forEach(event -> response.getFiles().add(event.getFile().getFilename()));

        return response;
    }
}
