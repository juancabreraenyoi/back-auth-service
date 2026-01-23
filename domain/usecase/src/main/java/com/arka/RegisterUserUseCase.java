package com.arka;

import com.arka.entities.User;
import com.arka.entities.enums.Role;
import com.arka.entities.request.Register;
import com.arka.entities.response.Auth;
import com.arka.exceptions.UserAlreadyExistException;
import com.arka.gateway.JwtGateway;
import com.arka.gateway.SecurityGateway;
import com.arka.gateway.UserGateway;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class RegisterUserUseCase {

   private final UserGateway userGateway;
   private final JwtGateway jwtGateway;
   private final SecurityGateway securityGateway;


    public Auth execute(Register register) {
        this.validateUserNotExists(register.getEmail());
        return registerNewUser(register);
    }

    private void validateUserNotExists(String email) {
        userGateway.findUserByEmail(email)
                .ifPresent(user -> {
                    throw new UserAlreadyExistException("User with email already exist!");
                });
    }

    private Auth registerNewUser(Register request) {
        User user = this.buildUser(request);
        User savedUser = userGateway.save(user);
        return this.buildAuthResponse(savedUser);
    }

    private User buildUser(Register request) {
        return User.builder()
                .email(request.getEmail())
                .password(this.encodePassword(request))
                .role(Role.USER)
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private Auth buildAuthResponse(User savedUser) {
        return Auth.builder()
                .accessToken(jwtGateway.generateToken(savedUser))
                .userResponse(savedUser)
                .build();
    }

    private String encodePassword(Register request) {
        return securityGateway.passwordEncoder()
                .encode(request.getPassword());
    }







}
