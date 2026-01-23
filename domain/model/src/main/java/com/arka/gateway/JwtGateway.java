package com.arka.gateway;

import com.arka.entities.User;

public interface JwtGateway {

    String generateToken(User user);

}
