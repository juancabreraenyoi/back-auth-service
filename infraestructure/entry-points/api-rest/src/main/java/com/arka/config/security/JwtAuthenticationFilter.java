package com.arka.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull  HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        Optional.ofNullable(request.getHeader("Authorization"))
                .ifPresentOrElse(  authHeader -> {
                    String token = authHeader.replaceAll("Bearer ", "");

                });





    }


}
