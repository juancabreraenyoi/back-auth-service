package com.arka.tables;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "password_reset_tokens", indexes = {@Index(name = "idx_token",columnList = "token",unique = true),
        @Index(name = "idx_user_id",columnList = "userId")})
public class PasswordResetTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "token",unique = true,nullable = false)
    private String token;

    @Column(name = "userId",nullable = false)
    private String userId;

    @Column(name = "expiryDate",nullable = false)
    private LocalDateTime expiredAt;

    @Column(name = "used",nullable = false)
    private boolean used;


}
