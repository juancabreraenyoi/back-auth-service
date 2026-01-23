package com.arka;

import com.arka.entities.User;
import com.arka.entities.request.Login;
import com.arka.entities.response.Auth;
import com.arka.exceptions.InvalidCredentialsException;
import com.arka.gateway.JwtGateway;
import com.arka.gateway.SecurityGateway;
import com.arka.gateway.UserGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.util.Objects;

@RequiredArgsConstructor
public class LoginUserUseCase {

    private final UserGateway userGateway;
    private final SecurityGateway securityGateway;
    private final JwtGateway jwtGateway;
    @Value("${jwt-provider.expiration-date}")
    private Long expired;

    public Auth execute(Login login) {
        return userGateway.findUserByEmail(login.getEmail())
                .filter(user -> isMatches(login.getPassword(), user.getPassword()))
                .filter(User::isEnabled)
                .map(this::buildAuthResponse)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));
    }

    private Auth buildAuthResponse(User user) {
        return Auth.builder()
                .accessToken(jwtGateway.generateToken(user))
                .userResponse(user)
                .expiresIn(expired)
                .build();
    }

    private boolean isMatches(String passwordLogin, String password ) {
        return securityGateway.passwordEncoder().matches(passwordLogin, password);
    }

}
