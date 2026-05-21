package com.oren.coupons.utils;

import com.oren.coupons.beans.SuccessfulLoginDetails;
import com.oren.coupons.consts.Consts;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class TestJWT {
    public static void main(String[] args) {
        try {
            // WARNING: test-only secret. Do not use this in production or commit secrets.
            String secret = Base64.getEncoder().encodeToString("this-is-a-test-secret-please-change".getBytes(StandardCharsets.UTF_8));
            Consts.JWT_KEY = secret;

            SuccessfulLoginDetails details = new SuccessfulLoginDetails(1, "tester@example.com", null, null);
            String token = JWTUtils.createJWT(details);
            System.out.println("Generated token: " + token);

            SuccessfulLoginDetails decoded = JWTUtils.decodeJWT(token);
            System.out.println("Decoded userName: " + decoded.getUserName());
            System.out.println("Decoded id: " + decoded.getId());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
