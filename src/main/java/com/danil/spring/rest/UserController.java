package com.danil.spring.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danil.spring.dto.CreateUserRequest;
import com.danil.spring.dto.GetAllUsersResponse;
import com.danil.spring.dto.GetUserResponse;
import com.danil.spring.dto.UpdateUserRequest;
import com.danil.spring.model.User;
import com.danil.spring.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasAuthority('user:create')")
    public ResponseEntity<Void> createUser(@RequestBody CreateUserRequest createUserRequest) {
        if (createUserRequest == null || !createUserRequest.valid()) {
            return ResponseEntity.badRequest().build();
        }
        userService.createUser(createUserRequest.getUsername(), createUserRequest.getPassword(),
                createUserRequest.getRole());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @PreAuthorize("hasAuthority('user:readall')")
    public ResponseEntity<List<GetAllUsersResponse>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        List<GetAllUsersResponse> response = new ArrayList<>();
        users.forEach(user -> response.add(GetAllUsersResponse.fromUser(user)));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasAuthority('user:readall') || hasAuthority('user:read') && #username == authentication.name")
    public ResponseEntity<GetUserResponse> getUser(@PathVariable String username) {
        User user = userService.findUser(username).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(GetUserResponse.fromUser(user));
    }

    @PutMapping("/{username}")
    @PreAuthorize("hasAuthority('user:update')")
    public ResponseEntity<Void> updateUser(@PathVariable String username, @RequestBody UpdateUserRequest updateUserRequest) {
        if (updateUserRequest == null || !updateUserRequest.valid()) {
            return ResponseEntity.badRequest().build();
        }
        userService.updateUser(username, updateUserRequest.getUsername(), updateUserRequest.getPassword(), updateUserRequest.getRole());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{username}")
    @PreAuthorize("hasAuthority('user:delete')")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return ResponseEntity.ok().build();
    }
}
