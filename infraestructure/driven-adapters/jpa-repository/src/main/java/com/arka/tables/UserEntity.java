package com.arka.tables;


import com.arka.entities.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user", indexes = {@Index(name = "idx_email",columnList = "email",unique = true)})
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", unique = true,nullable = false)
    private String email;

    @Column(name = "password",  nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Column(name = "enabled",nullable = false)
    private boolean enabled;

    @CreationTimestamp
    @Column(name = "createAt",nullable = false)
    private LocalDateTime createAt;

    @UpdateTimestamp
    @Column(name = "updateAt",nullable = false)
    private LocalDateTime updateAt;



}
