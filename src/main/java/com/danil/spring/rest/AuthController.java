package com.danil.spring.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danil.spring.dto.AuthRequest;
import com.danil.spring.dto.AuthResponse;
import com.danil.spring.dto.RegisterRequest;
import com.danil.spring.model.User;
import com.danil.spring.service.JwtService;
import com.danil.spring.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class AuthController {
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest registerRequest) {
        if (registerRequest == null || !registerRequest.valid()) {
            return ResponseEntity.badRequest().build();
        }
        userService.createUser(registerRequest.getUsername(), registerRequest.getPassword());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/auth")
    public ResponseEntity<AuthResponse> auth(@RequestBody AuthRequest authRequest) {
        if (authRequest == null || !authRequest.valid()) {
            return ResponseEntity.badRequest().build();
        }
        User user = userService.validateUser(authRequest.getUsername(), authRequest.getPassword()).orElse(null);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(AuthResponse.builder().token(token).build());
    }
}
