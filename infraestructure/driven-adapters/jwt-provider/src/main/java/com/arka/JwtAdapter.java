package com.arka;

import com.arka.entities.User;
import com.arka.gateway.JwtGateway;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.time.Clock;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtAdapter implements JwtGateway {

    @Value("${jwt-provider.expiration-date}")
    private Long expirationDate;

    @Value("${jwt-provider.secret}")
    private String secret;

    private final Clock clock;

    /**
     * Metodos para la gestion de un nuevo token, solicitado por un usuario
     * @param user
     * @return
     */
    public String generateToken(User user) {
       Map<String, Object> claims = Map.of("userId",user.getId(),"role",user.getRole());
       return this.createToken(user.getEmail(), claims);
    }

    private String createToken(String email, Map<String, Object> claims) {
        var dateNow = Date.from(clock.instant());
        var dateExpiration = new Date(dateNow.getTime() + expirationDate);

        return Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuedAt(dateNow)
                .expiration(dateExpiration)
                .signWith(this.getSiningKey())
                .compact();
    }

    /**
     * Metodos para validar o extraer valores puntuales del payload del token
     * @param token
     * @param userDetails
     * @return
     */


    public boolean isTokenValid(String token, UserDetails userDetails) {
        var email = extractEmail(token);
        return email.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpirationDate(token).toInstant().isBefore(Instant.now());

    }

    public String extractEmail(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public Date extractExpirationDate(String token){
        return extractClaims(token, Claims::getExpiration);
    }

    public String  extractRole(String token){
        return extractClaims(token, claims -> claims.get("role").toString());
    }

    public String extractUserId(String token){
        return extractClaims(token, claims -> claims.get("userId").toString());
    }


    /**
     * Metodos para resolver o extraer cualqueir claim del token validan la firma.
     * se retonara un objeto del tipo de claim solicictado
     * @param token
     * @param claimsResolver
     * @return
     * @param <T>
     */

    private <T> T extractClaims(String token, Function<Claims,T> claimsResolver){
        var claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(this.getSiningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Metodo para extraer la forma del token y que servira como llave para extraer informacion de token.
     * @return
     */

    private SecretKey getSiningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

}
