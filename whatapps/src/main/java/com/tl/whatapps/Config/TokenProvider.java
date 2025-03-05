package com.tl.whatapps.Config;

import java.util.Date;

import javax.crypto.SecretKey;

import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenProvider {

    SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

    public String generateToken(String email) {
        return Jwts.builder()
                .setIssuer("Dipen")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day expiry
                .claim("email", email)
                .signWith(key, SignatureAlgorithm.HS256) // Explicitly set the signing algorithm
                .compact();
    }




    public String getEmailFromToken(String jwt) {
        if (jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7); // Ensure "Bearer " is removed
        } else {
            throw new IllegalArgumentException("Token must start with 'Bearer '");
        }

        Claims claim = Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody();

        return claim.get("email", String.class);
    }


}