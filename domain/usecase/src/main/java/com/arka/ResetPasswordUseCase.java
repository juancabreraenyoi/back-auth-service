package com.arka;

import com.arka.entities.PasswordResetToken;
import com.arka.entities.User;
import com.arka.exceptions.UserNotFountException;
import com.arka.gateway.PasswordResetTokenGateway;
import com.arka.gateway.SecurityGateway;
import com.arka.gateway.UserGateway;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class ResetPasswordUseCase {

    private final PasswordResetTokenGateway passwordResetTokenGateway;
    private final UserGateway userGateway;
    private final SecurityGateway securityGateway;

    public void execute(String token, String newPassword) {
        var passwordResetToken = passwordResetTokenGateway.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired token"));

        if (passwordResetToken.isUsed()) {
            throw new RuntimeException("Token already used");
        }

        if (passwordResetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        var user = userGateway.findById(Long.parseLong(passwordResetToken.getUserId()))
                .orElseThrow(() -> new UserNotFountException("User not found"));

        user.setPassword(securityGateway.passwordEncoder().encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        userGateway.save(user);

        passwordResetToken.setUsed(true);
        passwordResetTokenGateway.save(passwordResetToken);
    }
}
