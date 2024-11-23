package com.exampleoctober.octoberproj.Security;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.exampleoctober.octoberproj.Registration.RegisterEntity.RegisterEntity;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${spring.security.jwt.secret}")
    private String secret;

    @Value("${spring.security.jwt.expiration}")
    private Long expiration;
    public String generateJwtToken(RegisterEntity user) {
        String token = JWT.create()
            .withSubject(user.getEmail())
            .withExpiresAt(new Date(System.currentTimeMillis() + expiration * 1000))
            .withClaim("email", user.getEmail())
            .withClaim("name", user.getName())
            .withClaim("number", user.getNumber())
            .sign(Algorithm.HMAC256(secret));
            // .compact(); // compact() method added
        logger.info("JWT token generated for user {}", user.getEmail());
        return token;
    }
    
    public boolean validateJwtToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            logger.info("JWT token validated successfully for user {}", decodedJWT.getSubject());
            return !decodedJWT.getExpiresAt().before(new Date());
        } catch (JWTVerificationException e) {
            logger.error("JWT token validation failed: {}", e.getMessage());
            return false;
        }
    }

    public String extractEmailFromToken(String token) {
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            logger.info("Email extracted from JWT token: {}", decodedJWT.getSubject());
            return decodedJWT.getSubject();
        } catch (Exception e) {
            logger.error("Error extracting email from JWT token: {}", e.getMessage());
            return null;
        }
    }

    public boolean isJwtTokenExpired(String token) {
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            logger.info("JWT token expiration checked");
            return decodedJWT.getExpiresAt().before(new Date());
        } catch (Exception e) {
            logger.error("Error checking JWT token expiration: {}", e.getMessage());
            return true;
        }
    }
}