package com.arka.repositorys;


import com.arka.tables.PasswordResetTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokenEntity,Long> {

    Optional<PasswordResetTokenEntity> findByToken(String token);
    void deleteByUserId(String userId);
    void deleteByUserEmail(String email);

}
