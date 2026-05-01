package com.example.final_project.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private SecretKey getSigningKey()
    {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /// generate a token
    public String generateToken(UserDetails userDetails, Long userID)
    {
        return Jwts.builder()
                .subject(String.valueOf(userID))
                .claim("email", userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    /// we need to extract the user id no username
    public Long extractUserId(String token)
    {
        return Long.parseLong(extractClaims(token).getSubject());
    }

    public String extractUsername(String token)
    {
        return extractClaims(token).get("email", String.class);
    }

    public Claims extractClaims(String token)
    {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /// now we need to validate the token
    public Boolean validateToken(String token, UserDetails userDetails)
    {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
}
