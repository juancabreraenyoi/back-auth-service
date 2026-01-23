package com.arka.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetToken {
    private Long id;
    private String token;
    private String userId;
    private LocalDateTime expiryDate;
    private boolean used;
}
