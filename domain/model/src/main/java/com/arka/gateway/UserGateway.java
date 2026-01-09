package com.arka.gateway;

import com.arka.entities.User;

public interface UserGateway {
    User findById(Long id);
    User findUserByEmail(String email);
    User save(User user);
}
