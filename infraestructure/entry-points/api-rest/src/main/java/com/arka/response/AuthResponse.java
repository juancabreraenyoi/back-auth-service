package com.arka.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String accessToken;
    private String tokenType;
    private Long expiresIn;
    private UserResponse userResponse;

    public static AuthResponse of(String accessToken,Long expiresIn,UserResponse userResponse){
       return AuthResponse.builder()
                .accessToken(accessToken)
                .expiresIn(expiresIn)
                .tokenType("Bearer")
                .userResponse(userResponse)
                .build();
    }
}
