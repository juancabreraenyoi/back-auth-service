package com.arka.controllers;

import com.arka.GetCurrentUserUseCase;
import com.arka.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final GetCurrentUserUseCase getCurrentUserUseCase;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication) {
        var email = authentication.getName();
        var user = getCurrentUserUseCase.execute(email);
        return ResponseEntity.ok(UserResponse.of(user));
    }
}
