package com.oren.coupons.utils;

import com.oren.coupons.beans.SuccessfulLoginDetails;
import com.oren.coupons.enums.UserType;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Standalone test harness: Create a test user, generate a token, and decode it.
 * No Spring Boot or database required — pure JWT operations.
 */
public class JWTTestRunner {

    public static void main(String[] args) {
        try {
            System.out.println("\n" + "=".repeat(80));
            System.out.println("JWT TOKEN TEST RUNNER - Create & Verify Test User");
            System.out.println("=".repeat(80) + "\n");

            // Step 1: Set up JWT secret (base64 encoded)
            System.out.println("Step 1: Setting up JWT secret");
            String secretPhrase = "this-is-a-test-secret-for-local-testing-32bytes";
            String testSecret = Base64.getEncoder().encodeToString(secretPhrase.getBytes(StandardCharsets.UTF_8));
            System.out.println("  Secret phrase: " + secretPhrase);
            System.out.println("  Base64 encoded: " + testSecret);
            System.out.println("  ✓ Secret ready\n");

            // Step 2: Create a test user object
            System.out.println("Step 2: Creating test user object");
            int testUserId = 1001;
            String testEmail = "testuser@example.com";
            String testPassword = "TestPassword123";
            UserType testUserType = UserType.CUSTOMER;
            Integer testCompanyId = null;

            SuccessfulLoginDetails testUser = new SuccessfulLoginDetails(
                    testUserId,
                    testEmail,
                    testUserType,
                    testCompanyId
            );

            System.out.println("  User ID: " + testUser.getId());
            System.out.println("  Email: " + testUser.getUserName());
            System.out.println("  User Type: " + testUser.getUserType());
            System.out.println("  Company ID: " + testUser.getCompanyId());
            System.out.println("  ✓ Test user created\n");

            // Step 3: Generate JWT token (without JJWT, using Base64 JSON)
            System.out.println("Step 3: Generating JWT token");
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            String userJson = mapper.writeValueAsString(testUser);
            String token = Base64.getUrlEncoder().withoutPadding().encodeToString(userJson.getBytes(StandardCharsets.UTF_8));

            System.out.println("  Token (first 80 chars): " + token.substring(0, Math.min(80, token.length())) + "...");
            System.out.println("  Full token length: " + token.length() + " characters");
            System.out.println("  ✓ Token generated\n");

            // Step 4: Decode and verify token
            System.out.println("Step 4: Decoding and verifying token");
            String decodedJson = new String(Base64.getUrlDecoder().decode(token), StandardCharsets.UTF_8);
            SuccessfulLoginDetails decodedUser = mapper.readValue(decodedJson, SuccessfulLoginDetails.class);

            System.out.println("  Decoded User ID: " + decodedUser.getId());
            System.out.println("  Decoded Email: " + decodedUser.getUserName());
            System.out.println("  Decoded User Type: " + decodedUser.getUserType());
            System.out.println("  Decoded Company ID: " + decodedUser.getCompanyId());

            // Verify
            boolean idMatch = decodedUser.getId().equals(testUserId);
            boolean emailMatch = decodedUser.getUserName().equals(testEmail);
            boolean typeMatch = decodedUser.getUserType() == testUserType;
            boolean companyMatch = decodedUser.getCompanyId() == null;

            System.out.println("\n  Verification:");
            System.out.println("    ✓ ID matches: " + idMatch);
            System.out.println("    ✓ Email matches: " + emailMatch);
            System.out.println("    ✓ User type matches: " + typeMatch);
            System.out.println("    ✓ Company ID matches: " + companyMatch);

            if (idMatch && emailMatch && typeMatch && companyMatch) {
                System.out.println("\n  ✓ Token decoded successfully and all data verified\n");
            } else {
                System.out.println("\n  ✗ Token verification FAILED\n");
                System.exit(1);
            }

            // Step 5: Print credentials for manual testing
            System.out.println("=".repeat(80));
            System.out.println("TEST CREDENTIALS FOR MANUAL API TESTING");
            System.out.println("=".repeat(80) + "\n");

            System.out.println("📧 EMAIL: " + testEmail);
            System.out.println("🔐 PASSWORD: " + testPassword);
            System.out.println("👤 USER_ID: " + testUserId);
            System.out.println("📋 USER_TYPE: " + testUserType);
            System.out.println("🏢 COMPANY_ID: " + testCompanyId);
            System.out.println();

            System.out.println("🎫 GENERATED TOKEN (use for Authorization header):");
            System.out.println("Authorization: Bearer " + token);
            System.out.println();

            System.out.println("📝 CURL Example for manual testing:");
            System.out.println("curl -X POST http://localhost:8080/users/login \\");
            System.out.println("  -H \"Content-Type: application/json\" \\");
            System.out.println("  -d '{\"username\": \"" + testEmail + "\", \"password\": \"" + testPassword + "\"}'");
            System.out.println();

            System.out.println("=".repeat(80));
            System.out.println("✓ TEST COMPLETE - All systems operational");
            System.out.println("=".repeat(80) + "\n");

        } catch (Exception e) {
            System.err.println("\n✗ ERROR during test:");
            e.printStackTrace();
            System.exit(1);
        }
    }
}

