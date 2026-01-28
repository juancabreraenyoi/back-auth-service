package com.arka.controllers;

import com.arka.AssignRoleUseCase;
import com.arka.request.AssignRoleRequest;
import com.arka.response.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AssignRoleUseCase assignRoleUseCase;

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/users/{userId}/role")
    public ResponseEntity<UserResponse> assignRole(
            @PathVariable Long userId,
            @Valid @RequestBody AssignRoleRequest request
    ) {
        var user = assignRoleUseCase.execute(userId, request.getRole());
        return ResponseEntity.ok(UserResponse.of(user));
    }
}
