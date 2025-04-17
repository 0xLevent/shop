package com.example.market.Security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    public String generateToken(String email, int userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId); // Store the user ID in the token
        return createToken(claims, email);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }

    // Extract username from token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract expiration date from token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extract userId from token
    public Integer extractUserId(String jwt) {
        final Claims claims = extractAllClaims(jwt);  // Claims'i çıkar
        return claims.get("userId", Integer.class);   // "userId"yi Integer olarak alın
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Check if the token has expired
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Validate token
    public Boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
}