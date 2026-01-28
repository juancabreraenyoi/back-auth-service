package com.arka;

import com.arka.entities.User;
import com.arka.entities.enums.Role;
import com.arka.exceptions.UserNotFountException;
import com.arka.gateway.UserGateway;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class AssignRoleUseCase {

    private final UserGateway userGateway;

    public User execute(Long userId, Role role) {
        var user = userGateway.findById(userId)
                .orElseThrow(() -> new UserNotFountException("User not found with id: " + userId));

        user.toBuilder()
                .role(role)
                .updatedAt(LocalDateTime.now())
                .build();
        return userGateway.save(user);
    }
}
