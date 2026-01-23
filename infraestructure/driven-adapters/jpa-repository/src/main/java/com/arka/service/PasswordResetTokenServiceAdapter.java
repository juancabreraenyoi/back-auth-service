package com.arka.service;

import com.arka.entities.PasswordResetToken;
import com.arka.gateway.PasswordResetTokenGateway;
import com.arka.repositorys.PasswordResetTokenRepository;
import com.arka.tables.PasswordResetTokenEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PasswordResetTokenServiceAdapter implements PasswordResetTokenGateway {

    private final PasswordResetTokenRepository passwordResetTokenRepository;


    @Override
    public Optional<PasswordResetToken> findByToken(String token) {
        return passwordResetTokenRepository.findByToken(token)
                .map(this::toDomain);
    }

    @Override
    public PasswordResetToken save(PasswordResetToken passwordResetToken) {
        var entity = toEntity(passwordResetToken);
        var saved = passwordResetTokenRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public void delete(PasswordResetToken passwordResetToken) {

    }

    @Override
    public void deleteByUser(String email) {
        passwordResetTokenRepository.deleteByUserEmail(email);
    }

    /**
     * Aqui creamos un mapeo manual pero debe estar en otro contexto y servir para cualquier contexto de dominio y
     * adaptador -- se puede usar la libreria https://mapstruct.org/
     * @param passwordResetTokenEntity
     * @return
     */
    private PasswordResetToken toDomain(PasswordResetTokenEntity passwordResetTokenEntity) {
        return PasswordResetToken.builder()
                .token(passwordResetTokenEntity.getToken())
                .id(passwordResetTokenEntity.getId())
                .userId(passwordResetTokenEntity.getUserId())
                .expiryDate(passwordResetTokenEntity.getExpiredAt())
                .used(passwordResetTokenEntity.isUsed())
                .build();
    }

    /**
     * Aqui creamos un mapeo manual pero debe estar en otro contexto y servir para cualquier contexto de dominio y
     * adaptador -- se puede usar la libreria https://mapstruct.org/
     * @param passwordResetToken
     * @return
     */
    private PasswordResetTokenEntity toEntity(PasswordResetToken passwordResetToken) {
        return PasswordResetTokenEntity.builder()
                .token(passwordResetToken.getToken())
                .id(passwordResetToken.getId())
                .userId(passwordResetToken.getUserId())
                .used(passwordResetToken.isUsed())
                .expiredAt(passwordResetToken.getExpiryDate())
                .build();
    }
}
