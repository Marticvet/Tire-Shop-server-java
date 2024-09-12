package com.tireshop.tiresShop.service.security;

import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
//import java.security.KeyPair;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.tireshop.tiresShop.service.model.UserEntity;

@Component
public class JWTGenerator {

    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    // public String generateToken(Authentication authentication) {
    // String username = authentication.getName();
    // Date currentDate = new Date();
    // Date expireDate = new Date(currentDate.getTime() +
    // SecurityConstants.JWT_EXPIRATION);

    // String token = Jwts.builder()
    // .setSubject(username)
    // .setIssuedAt(new Date())
    // .setExpiration(expireDate)
    // .signWith(key, SignatureAlgorithm.HS512)
    // .compact();

    // return token;
    // }

    public String generateToken(Authentication authentication, UserEntity user) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + SecurityConstants.JWT_EXPIRATION);

        // Create the token with additional claims (userId, firstName, lastName)
        return Jwts.builder()
                .setSubject(username)
                .claim("userId", user.getUserId()) // Add userId
                .claim("firstName", user.getFirstName()) // Add firstName
                .claim("lastName", user.getLastName()) // Add lastName
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            throw new AuthenticationCredentialsNotFoundException("JWT was exprired or incorrect",
                    ex.fillInStackTrace());
        }
    }

    // added later

    public String getFirstNameFromJWT(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("firstName", String.class);
    }

    public String getLastNameFromJWT(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("lastName", String.class);
    }

    public Integer getUserIdFromJWT(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("userId", Integer.class);
    }
}
