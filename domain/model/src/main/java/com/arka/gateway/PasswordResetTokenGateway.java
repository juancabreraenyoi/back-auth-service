package com.arka.gateway;

import com.arka.entities.PasswordResetToken;

public interface PasswordResetTokenGateway {
    PasswordResetToken findByToken(String token);
    PasswordResetToken save(PasswordResetToken passwordResetToken);
    void delete(PasswordResetToken passwordResetToken);
}
