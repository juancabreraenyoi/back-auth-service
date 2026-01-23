package com.arka.entities.response;

import com.arka.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Auth {

    private String accessToken;
    private String tokenType;
    private Long expiresIn;
    private User userResponse;
}
