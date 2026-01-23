package com.arka.gateway;

import com.arka.entities.User;

import java.util.Optional;

public interface UserGateway {
    Optional<User> findById(Long id);
    Optional<User> findUserByEmail(String email);
    User save(User user);
}
