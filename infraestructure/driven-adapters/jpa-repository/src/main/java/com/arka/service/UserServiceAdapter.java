package com.arka.service;

import com.arka.entities.User;
import com.arka.gateway.UserGateway;
import com.arka.repositorys.UserRepository;
import com.arka.tables.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserServiceAdapter implements UserGateway {

    private final UserRepository userRepository;

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::toDomain);
    }

    @Override
    public User save(User user) {
        var entity = toEntity(user);
        var saved = userRepository.save(entity);
        return toDomain(saved);
    }

    /**
     * Aqui creamos un mapeo manual pero debe estar en otro contexto y servir para cualquier contexto de dominio y
     * adaptador -- se puede usar la libreria https://mapstruct.org/
     * @param user
     * @return
     */
    private UserEntity toEntity(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .enabled(user.isEnabled())
                .createAt(user.getCreatedAt())
                .updateAt(user.getUpdatedAt())
                .build();
    }

    /**
     * Aqui creamos un mapeo manual pero debe estar en otro contexto y servir para cualquier contexto de dominio y
     * adaptador -- se puede usar la libreria https://mapstruct.org/
     * @param userEntity
     * @return
     */
    private User toDomain(UserEntity userEntity) {
        return User.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .role(userEntity.getRole())
                .enabled(userEntity.isEnabled())
                .createdAt(userEntity.getCreateAt())
                .updatedAt(userEntity.getUpdateAt())
                .build();

    }
}
