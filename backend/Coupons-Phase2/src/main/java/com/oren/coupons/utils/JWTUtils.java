package com.oren.coupons.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oren.coupons.beans.SuccessfulLoginDetails;
import com.oren.coupons.consts.Consts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

public class JWTUtils {

    public static Claims decodeJWTClaims(String jwt) throws Exception {
        String tokenWithoutBearer = getTokenWithoutBearer(jwt);
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(tokenWithoutBearer)
                .getBody();

        Date expirationDate = claims.getExpiration();
        if (expirationDate != null && expirationDate.before(new Date())) {
            throw new Exception("Token has expired");
        }

        return claims;
    }

    public static SuccessfulLoginDetails decodeJWT(String jwt) throws Exception {
        Claims claims = decodeJWTClaims(jwt);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(claims.getSubject(), SuccessfulLoginDetails.class);
    }

    public static String createJWT(SuccessfulLoginDetails successfulLoginDetails) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonLoginDetails = objectMapper.writeValueAsString(successfulLoginDetails);
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        return Jwts.builder()
                .setSubject(jsonLoginDetails)
                .setIssuedAt(now)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public static void validateToken(String token) throws Exception {
        decodeJWTClaims(token);
    }

    private static SecretKey getSigningKey() {
        // Handle both plain text and base64-encoded secrets
        byte[] keyBytes;
        try {
            // Try to decode as base64 first
            keyBytes = Base64.getDecoder().decode(Consts.JWT_KEY);
        } catch (IllegalArgumentException e) {
            // If base64 decode fails, treat as plain text and encode it
            keyBytes = Consts.JWT_KEY.getBytes(StandardCharsets.UTF_8);
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private static String getTokenWithoutBearer(String token) {
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Token cannot be null or empty");
        }

        if (token.startsWith("Bearer ")) {
            String[] textSegments = token.split(" ");
            if (textSegments.length < 2) {
                throw new IllegalArgumentException("Malformed Bearer token");
            }
            return textSegments[1];
        }
        return token;
    }
}
