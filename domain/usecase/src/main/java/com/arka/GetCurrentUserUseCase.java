package com.arka;

import com.arka.entities.User;
import com.arka.exceptions.UserNotFountException;
import com.arka.gateway.UserGateway;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetCurrentUserUseCase {

    private final UserGateway userGateway;

    public User execute(String email) {
        return userGateway.findUserByEmail(email)
                .orElseThrow( () -> new  UserNotFountException("User no exist!"));
    }

}
