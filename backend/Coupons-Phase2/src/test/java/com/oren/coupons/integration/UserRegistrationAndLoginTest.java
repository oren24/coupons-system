package com.oren.coupons.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oren.coupons.beans.SuccessfulLoginDetails;
import com.oren.coupons.consts.Consts;
import com.oren.coupons.dto.User;
import com.oren.coupons.enums.UserType;
import com.oren.coupons.utils.JWTUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration test: Create a user, login, and verify the token works.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class UserRegistrationAndLoginTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String TEST_EMAIL = "testuser" + System.currentTimeMillis() + "@example.com";
    private static final String TEST_PASSWORD = "SecurePass123";
    private static final String API_USERS = "/users";
    private static final String API_LOGIN = "/users/login";

    @BeforeEach
    public void setup() {
        // Set a test JWT secret (base64 encoded)
        String testSecret = Base64.getEncoder().encodeToString("test-secret-for-integration-testing-32bytes".getBytes(StandardCharsets.UTF_8));
        Consts.JWT_KEY = testSecret;
    }

    @Test
    public void testUserRegistrationAndLogin() throws Exception {
        System.out.println("\n=== Starting User Registration and Login Test ===\n");

        // Step 1: Create a new user
        System.out.println("Step 1: Creating new user with email: " + TEST_EMAIL);
        User newUser = new User(TEST_EMAIL, TEST_PASSWORD, UserType.CUSTOMER);
        String userJson = objectMapper.writeValueAsString(newUser);

        MvcResult createResult = mockMvc.perform(post(API_USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println("✓ User created successfully (HTTP 200)");

        // Step 2: Login with the created credentials
        System.out.println("\nStep 2: Logging in with credentials");
        User loginRequest = new User(TEST_EMAIL, TEST_PASSWORD, null);
        String loginJson = objectMapper.writeValueAsString(loginRequest);

        MvcResult loginResult = mockMvc.perform(post(API_LOGIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.token").exists())
                .andReturn();

        String responseBody = loginResult.getResponse().getContentAsString();
        System.out.println("✓ Login successful (HTTP 200)");
        System.out.println("Response: " + responseBody);

        @SuppressWarnings("unchecked")
        Map<String, Object> loginResponse = objectMapper.readValue(responseBody, Map.class);
        String token = (String) loginResponse.get("token");

        assertThat(token).isNotNull().isNotEmpty();
        System.out.println("✓ Token received: " + token.substring(0, Math.min(50, token.length())) + "...");

        // Step 3: Decode the JWT token to verify it contains correct user info
        System.out.println("\nStep 3: Decoding and verifying JWT token");
        SuccessfulLoginDetails decodedUser = JWTUtils.decodeJWT(token);

        System.out.println("Decoded token details:");
        System.out.println("  - Username: " + decodedUser.getUserName());
        System.out.println("  - User ID: " + decodedUser.getId());
        System.out.println("  - User Type: " + decodedUser.getUserType());
        System.out.println("  - Company ID: " + decodedUser.getCompanyId());

        assertThat(decodedUser.getUserName()).isEqualTo(TEST_EMAIL);
        assertThat(decodedUser.getUserType()).isEqualTo(UserType.CUSTOMER);
        assertThat(decodedUser.getId()).isNotNull().isGreaterThan(0);

        System.out.println("\n✓ Token decoded successfully and contains correct user information");
        System.out.println("\n=== Test Passed: User Registration and Login Flow Complete ===\n");
    }
}

