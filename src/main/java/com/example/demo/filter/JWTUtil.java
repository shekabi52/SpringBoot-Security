package com.example.demo.filter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JWTUtil {

    private static final String SECRET_KEY = "abishekdossantosdeavieroabishekdossantosdeavieroabishekdossantosdeavieroabishekdossantosdeavieroabishekdossantosdeavieroabishekdossantosdeaviero";
    private static Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));


    public String generateToken(String username, long expiryTime){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiryTime*60*1000))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String validateAndExtarctUserName(String token){
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
