# Security Fixes - Implementation Guide

## Quick Reference for Critical Fixes

---

## 1. Password Hashing Fix (IMMEDIATE)

### Current Code (INSECURE)
```java
// HashFunction.java
public static String hash(String password) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    byte[] bytes = password.getBytes(StandardCharsets.UTF_8);
    byte[] hash = md.digest(bytes);
    // ... converts to hex string
}
```

### Fixed Code
```java
// HashFunction.java
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HashFunction {
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    
    public static String hash(String password) {
        return encoder.encode(password);
    }
    
    public static boolean verify(String rawPassword, String hashedPassword) {
        return encoder.matches(rawPassword, hashedPassword);
    }
}
```

### Update pom.xml
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

### Update UserLogic.java
```java
// In login method
public String login(String userName, String password) throws ApplicationException {
    User user = this.usersDal.getUserByUsername(userName);
    
    if (user == null || !HashFunction.verify(password, user.getPassword())) {
        throw new ApplicationException(ErrorType.USER_NOT_FOUND, 
            "username and password do not match");
    }
    
    try {
        String token = generateToken(userName, user.getPassword());  // Use hashed from DB
        return token;
    } catch (Exception e) {
        throw new ApplicationException(ErrorType.GENERAL_ERROR, "Could not create token", e);
    }
}
```

---

## 2. Move JWT Secret to Environment Variables (IMMEDIATE)

### Step 1: Update Consts.java
```java
// Consts.java
package com.oren.coupons.consts;

public class Consts {
    public static final String JWT_KEY = getJwtSecretFromEnv();
    
    private static String getJwtSecretFromEnv() {
        String secret = System.getenv("JWT_SECRET_KEY");
        if (secret == null || secret.isEmpty()) {
            throw new IllegalStateException(
                "JWT_SECRET_KEY environment variable not set. " +
                "Please set: export JWT_SECRET_KEY='your-secret-key-min-32-chars'"
            );
        }
        if (secret.length() < 32) {
            throw new IllegalStateException("JWT_SECRET_KEY must be at least 32 characters");
        }
        return secret;
    }
    
    public static final long JWT_EXPIRATION_MS = getJwtExpirationFromEnv();
    
    private static long getJwtExpirationFromEnv() {
        String expiration = System.getenv("JWT_EXPIRATION_MS");
        return expiration != null ? Long.parseLong(expiration) : 3600000; // 1 hour default
    }
}
```

### Step 2: Update application.properties
```properties
# application.properties (use Spring placeholders)
jwt.secret=${JWT_SECRET_KEY:default-dev-only-secret-do-not-use-in-prod}
jwt.expiration=${JWT_EXPIRATION_MS:3600000}
```

### Step 3: Set Environment Variable
```bash
# Linux/Mac
export JWT_SECRET_KEY="your-very-long-secret-key-with-at-least-32-characters-$(openssl rand -hex 16)"

# Windows
set JWT_SECRET_KEY=your-very-long-secret-key-with-at-least-32-characters-$(PowerShell -Command "[System.Convert]::ToHexString((1..16|%{Get-Random -Max 256}))")

# Or in .env file (add to .gitignore)
JWT_SECRET_KEY=your-secret-here
JWT_EXPIRATION_MS=3600000
```

### Step 4: Clean Git History
```bash
# This removes the secret from Git history (IMPORTANT!)
git filter-branch --force --index-filter \
  "git rm --cached --ignore-unmatch backend/Coupons-Phase2/src/main/java/com/oren/coupons/consts/Consts.java" \
  --prune-empty --tag-name-filter cat -- --all

# Or use BFG Repo-Cleaner (easier)
# Download: https://rtyley.github.io/bfg-repo-cleaner/
bfg --delete-files Consts.java
```

---

## 3. Add Authorization to Controllers (HIGH PRIORITY)

### CouponsController.java
```java
package com.oren.coupons.controllers;

import com.oren.coupons.dto.Coupon;
import com.oren.coupons.exceptions.ApplicationException;
import com.oren.coupons.logic.CouponLogic;
import com.oren.coupons.encryptions.tokenConverters;
import com.oren.coupons.enums.UserType;
import com.oren.coupons.enums.ErrorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/coupons")
public class CouponsController {

    private final CouponLogic couponLogic;

    @Autowired
    public CouponsController(CouponLogic couponLogic) {
        this.couponLogic = couponLogic;
    }

    @PostMapping
    public void createCoupon(
        @RequestHeader("Authorization") String token,
        @RequestBody Coupon coupon) throws ApplicationException {
        
        UserType userType = tokenConverters.getUserTypeFromToken(token);
        
        // Only COMPANY and ADMIN can create coupons
        if (userType != UserType.COMPANY && userType != UserType.ADMIN) {
            throw new ApplicationException(ErrorType.UNAUTHORIZED, 
                "Only companies and admins can create coupons");
        }
        
        // COMPANY users can only create for their own company
        if (userType == UserType.COMPANY) {
            Integer companyId = tokenConverters.getCompanyIdFromToken(token);
            if (!companyId.equals(coupon.getCompanyId())) {
                throw new ApplicationException(ErrorType.UNAUTHORIZED, 
                    "Cannot create coupons for other companies");
            }
        }
        
        this.couponLogic.addCoupon(coupon);
    }

    @PutMapping
    public void updateCoupon(
        @RequestHeader("Authorization") String token,
        @RequestBody Coupon coupon) throws ApplicationException {
        
        UserType userType = tokenConverters.getUserTypeFromToken(token);
        
        if (userType == UserType.COMPANY) {
            Integer companyId = tokenConverters.getCompanyIdFromToken(token);
            if (!companyId.equals(coupon.getCompanyId())) {
                throw new ApplicationException(ErrorType.UNAUTHORIZED, 
                    "Cannot update coupons for other companies");
            }
        } else if (userType == UserType.CUSTOMER) {
            throw new ApplicationException(ErrorType.UNAUTHORIZED, 
                "Customers cannot update coupons");
        }
        
        this.couponLogic.updateCoupon(coupon);
    }

    @GetMapping
    public List<Coupon> getAllCoupons() throws ApplicationException {
        return this.couponLogic.getAllCoupons();
    }

    @GetMapping("/byCompanyId")
    public List<Coupon> getCouponsByCompanyId(@RequestParam("companyId") int id) 
        throws ApplicationException {
        return this.couponLogic.getAllCouponsByCompanyId(id);
    }

    @GetMapping("/{id}")
    public Coupon getCoupon(@PathVariable("id") int id) throws ApplicationException {
        return this.couponLogic.getCoupon(id);
    }

    @DeleteMapping("/{id}")
    public void deleteCoupon(
        @RequestHeader("Authorization") String token,
        @PathVariable("id") int id) throws ApplicationException {
        
        UserType userType = tokenConverters.getUserTypeFromToken(token);
        
        if (userType == UserType.CUSTOMER) {
            throw new ApplicationException(ErrorType.UNAUTHORIZED, 
                "Customers cannot delete coupons");
        }
        
        if (userType == UserType.COMPANY) {
            Integer companyId = tokenConverters.getCompanyIdFromToken(token);
            Coupon coupon = this.couponLogic.getCoupon(id);
            
            if (!companyId.equals(coupon.getCompanyId())) {
                throw new ApplicationException(ErrorType.UNAUTHORIZED, 
                    "Cannot delete coupons from other companies");
            }
        }
        
        this.couponLogic.deleteCoupon(id);
    }
}
```

### CompaniesController.java
```java
package com.oren.coupons.controllers;

import com.oren.coupons.dto.Company;
import com.oren.coupons.exceptions.ApplicationException;
import com.oren.coupons.logic.CompanyLogic;
import com.oren.coupons.encryptions.tokenConverters;
import com.oren.coupons.enums.UserType;
import com.oren.coupons.enums.ErrorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompaniesController {

    private final CompanyLogic companyLogic;

    @Autowired
    public CompaniesController(CompanyLogic companyLogic) {
        this.companyLogic = companyLogic;
    }

    @PostMapping()
    public void createCompany(
        @RequestHeader("Authorization") String token,
        @RequestBody Company company) throws ApplicationException {
        
        UserType userType = tokenConverters.getUserTypeFromToken(token);
        if (userType != UserType.ADMIN) {
            throw new ApplicationException(ErrorType.UNAUTHORIZED, 
                "Only admins can create companies");
        }
        
        this.companyLogic.addCompany(company);
    }

    @PutMapping
    public void updateCompany(
        @RequestHeader("Authorization") String token,
        @RequestBody Company company) throws ApplicationException {
        
        UserType userType = tokenConverters.getUserTypeFromToken(token);
        if (userType != UserType.ADMIN) {
            throw new ApplicationException(ErrorType.UNAUTHORIZED, 
                "Only admins can update companies");
        }
        
        this.companyLogic.updateCompany(company);
    }

    @GetMapping()
    public List<Company> getAllCompanies() throws ApplicationException {
        return this.companyLogic.getAllCompanies();
    }

    @GetMapping("/{Id}")
    public Company getCompany(@PathVariable("Id") int id) throws ApplicationException {
        return this.companyLogic.getCompany(id);
    }

    @DeleteMapping("/{Id}")
    public void deleteCompany(
        @RequestHeader("Authorization") String token,
        @PathVariable("Id") int companyId) throws ApplicationException {
        
        UserType userType = tokenConverters.getUserTypeFromToken(token);
        if (userType != UserType.ADMIN) {
            throw new ApplicationException(ErrorType.UNAUTHORIZED, 
                "Only admins can delete companies");
        }
        
        this.companyLogic.deleteCompany(companyId);
    }
}
```

---

## 4. Fix Token Expiration Validation (HIGH PRIORITY)

### JWTUtils.java
```java
package com.oren.coupons.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oren.coupons.beans.SuccessfulLoginDetails;
import com.oren.coupons.consts.Consts;
import io.jsonwebtoken.*;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

public class JWTUtils {
    
    public static Claims decodeJWTClaims(String jwt) throws ExpiredJwtException, JwtException {
        try {
            Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(Consts.JWT_KEY))
                .parseClaimsJws(jwt)
                .getBody();
            
            // Explicitly check expiration
            if (claims.getExpiration() != null && claims.getExpiration().before(new Date())) {
                throw new ExpiredJwtException(null, claims, "Token has expired");
            }
            
            return claims;
        } catch (ExpiredJwtException e) {
            throw e;
        } catch (JwtException e) {
            throw new JwtException("Invalid token: " + e.getMessage(), e);
        }
    }

    public static SuccessfulLoginDetails decodeJWT(String jwt) throws Exception {
        String tokenWithoutBearer = getTokenWithoutBearer(jwt);
        Claims claims = decodeJWTClaims(tokenWithoutBearer);
        ObjectMapper objectMapper = new ObjectMapper();
        SuccessfulLoginDetails successfulLoginDetails = 
            objectMapper.readValue(claims.getSubject(), SuccessfulLoginDetails.class);
        return successfulLoginDetails;
    }

    public static String createJWT(SuccessfulLoginDetails successfulLoginDetails) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonLoginDetails = objectMapper.writeValueAsString(successfulLoginDetails);
        return createJWT("0", successfulLoginDetails.getUserName(), jsonLoginDetails, Consts.JWT_EXPIRATION_MS);
    }

    private static String createJWT(String id, String issuer, String subject, long ttlMillis) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(Consts.JWT_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        JwtBuilder builder = Jwts.builder()
            .setId(id)
            .setIssuedAt(now)
            .setSubject(subject)
            .setIssuer(issuer)
            .signWith(signatureAlgorithm, signingKey);

        if (ttlMillis > 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        return builder.compact();
    }

    public static void validateToken(String token) throws Exception {
        String tokenWithoutBearer = getTokenWithoutBearer(token);
        // This will throw ExpiredJwtException if token is expired
        decodeJWTClaims(tokenWithoutBearer);
    }

    private static String getTokenWithoutBearer(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7); // "Bearer ".length() == 7
        }
        return token;
    }
}
```

---

## 5. Fix Login Response Format (HIGH PRIORITY)

### Backend: UserController.java
```java
@PostMapping("/login")
public ResponseEntity<Map<String, String>> login(@RequestBody User user) 
    throws ApplicationException {
    
    String token = userLogic.login(user.getUsername(), user.getPassword());
    
    return ResponseEntity.ok(Collections.singletonMap("token", token));
}
```

### Frontend: Login.tsx
```typescript
import "./Login.css"
import {useState} from "react";
import Modal from "react-modal";
import axios from "axios";
import jwt_decode from "jwt-decode";
import IUser from "../../models/IUser";

export function Login() {
    let [username, setUserName] = useState('');
    let [password, setPassword] = useState('');
    let [isLoginFailed, setIsLoginFailed] = useState(false);
    let [errorMessage, setErrorMessage] = useState('');

    const onLoginClicked = async () => {
        try {
            setIsLoginFailed(false);
            setErrorMessage('');

            // Validate inputs
            if (!username || !password) {
                setErrorMessage('Username and password are required');
                setIsLoginFailed(true);
                return;
            }

            const response = await axios.post("http://localhost:8080/users/login", 
                {username, password});

            // Fixed: Response now contains { token: "..." }
            const token = response.data.token;
            if (!token) {
                setErrorMessage('No token received from server');
                setIsLoginFailed(true);
                return;
            }

            // Store token
            const bearerToken = 'Bearer ' + token;
            axios.defaults.headers.common['Authorization'] = bearerToken;
            localStorage.setItem('token', bearerToken);

            // Decode and store user data
            const decoded: any = jwt_decode(token);
            const userData: IUser = decoded.sub;

            // Dispatch login action to Redux or redirect
            window.location.href = "/dashboard";

        } catch (error: any) {
            console.error('Login error:', error);
            if (error.response?.status === 401) {
                setErrorMessage('Invalid username or password');
            } else if (error.response?.data?.message) {
                setErrorMessage(error.response.data.message);
            } else {
                setErrorMessage('Login failed. Please try again.');
            }
            setIsLoginFailed(true);
        }
    }

    return (
        <div className="Login">
            <h2>Login</h2>
            <p>Already registered? Log in here!</p>
            <h3>Enter your username and password</h3>

            {isLoginFailed && <div className="error-message">{errorMessage}</div>}

            <label>
                Username<br/>
                <input 
                    type="email" 
                    placeholder='username@example.com' 
                    onChange={event => setUserName(event.target.value)}
                    disabled={isLoginFailed}
                    style={isLoginFailed ? {border: '1px solid red'} : {}}
                />
            </label>
            <br/>

            <label>
                Password<br/>
                <input 
                    type="password" 
                    placeholder='password' 
                    onChange={event => setPassword(event.target.value)}
                    disabled={isLoginFailed}
                    style={isLoginFailed ? {border: '1px solid red'} : {}}
                />
            </label>
            <br/>

            <button onClick={onLoginClicked} disabled={isLoginFailed}>
                Submit
            </button>
        </div>
    );
}
```

---

## 6. Add Security Headers Filter (HIGH PRIORITY)

### New File: SecurityHeadersFilter.java
```java
package com.oren.coupons.filters;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SecurityHeadersFilter extends OncePerRequestFilter {
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                   HttpServletResponse response, 
                                   FilterChain filterChain) 
        throws ServletException, IOException {
        
        // Prevent MIME type sniffing
        response.setHeader("X-Content-Type-Options", "nosniff");
        
        // Prevent clickjacking
        response.setHeader("X-Frame-Options", "DENY");
        
        // Enable XSS protection
        response.setHeader("X-XSS-Protection", "1; mode=block");
        
        // Force HTTPS
        response.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains; preload");
        
        // Content Security Policy
        response.setHeader("Content-Security-Policy", 
            "default-src 'self'; script-src 'self' 'unsafe-inline'; style-src 'self' 'unsafe-inline'");
        
        // Referrer policy
        response.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
        
        // Feature policy
        response.setHeader("Permissions-Policy", "geolocation=(), microphone=(), camera=()");
        
        filterChain.doFilter(request, response);
    }
}
```

---

## 7. Fix CORS Configuration (MEDIUM PRIORITY)

### Updated CORSFilter.java
```java
package com.oren.coupons.filters;

import org.springframework.stereotype.Component;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CORSFilter implements Filter {
    
    private static final String ALLOWED_ORIGINS = 
        System.getenv("CORS_ALLOWED_ORIGINS") != null 
            ? System.getenv("CORS_ALLOWED_ORIGINS")
            : "http://localhost:3000";

    @Override
    public void doFilter(ServletRequest servletRequest, 
                        ServletResponse servletResponse, 
                        FilterChain chain) throws IOException, ServletException {
        
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        
        String origin = request.getHeader("Origin");
        
        // Validate origin against whitelist
        if (origin != null && isOriginAllowed(origin)) {
            response.addHeader("Access-Control-Allow-Origin", origin);
            response.addHeader("Access-Control-Allow-Credentials", "true");
            response.addHeader("Access-Control-Allow-Methods", 
                "GET, OPTIONS, HEAD, PUT, POST, DELETE");
            response.addHeader("Access-Control-Allow-Headers", 
                "Origin, Accept, x-auth-token, Content-Type, Authorization");
            response.addHeader("Access-Control-Max-Age", "3600");
        }
        
        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            return;
        }
        
        chain.doFilter(request, servletResponse);
    }
    
    private boolean isOriginAllowed(String origin) {
        String[] allowedOrigins = ALLOWED_ORIGINS.split(",");
        for (String allowed : allowedOrigins) {
            if (allowed.trim().equals(origin)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void init(FilterConfig fConfig) throws ServletException {}

    @Override
    public void destroy() {}
}
```

### application.properties
```properties
# CORS Configuration
cors.allowed-origins=http://localhost:3000,https://yourdomain.com
```

---

## 8. Improve Error Handling (MEDIUM PRIORITY)

### Updated ExceptionsHandler.java
```java
package com.oren.coupons.exceptions;

import com.oren.coupons.beans.ErrorBean;
import com.oren.coupons.enums.ErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;

@RestControllerAdvice
public class ExceptionsHandler {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionsHandler.class);

    @ExceptionHandler(ApplicationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorBean> handleApplicationException(
        ApplicationException exception,
        HttpServletResponse response) {
        
        ErrorType errorType = exception.getErrorType();
        int statusCode = errorType.getErrorNumber();
        String message = errorType.getErrorMessage();
        
        // Log with full details internally
        logger.warn("Application exception: {} - {}", statusCode, message, exception);
        
        // Don't return stack trace to client
        ErrorBean errorBean = new ErrorBean(statusCode, message);
        return new ResponseEntity<>(errorBean, HttpStatus.valueOf(mapToHttpStatus(statusCode)));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorBean> handleGenericException(
        Exception exception,
        HttpServletResponse response) {
        
        // Log with full details internally
        logger.error("Unexpected exception: ", exception);
        
        // Generic message to client
        String message = "An internal server error occurred";
        ErrorBean errorBean = new ErrorBean(500, message);
        
        return new ResponseEntity<>(errorBean, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private int mapToHttpStatus(int errorCode) {
        if (errorCode >= 400 && errorCode < 600) {
            return errorCode;
        }
        return 500;  // Default to internal server error
    }
}
```

---

## Environment Variables Setup

### .env file (add to .gitignore)
```bash
# JWT Configuration
JWT_SECRET_KEY=your-very-long-random-secret-key-at-least-32-characters-generated-securely
JWT_EXPIRATION_MS=3600000

# Database Configuration
DB_URL=jdbc:mysql://localhost:3306/coupons?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
DB_USERNAME=root
DB_PASSWORD=your-secure-password

# CORS Configuration
CORS_ALLOWED_ORIGINS=http://localhost:3000

# SSL Configuration
SSL_PASSWORD=your-keystore-password
```

### application.properties (use placeholders)
```properties
# JWT
jwt.secret=${JWT_SECRET_KEY}
jwt.expiration=${JWT_EXPIRATION_MS}

# Database
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

# CORS
cors.allowed-origins=${CORS_ALLOWED_ORIGINS}
```

---

## Testing the Fixes

### 1. Test Password Hashing
```java
@Test
public void testPasswordHashing() {
    String password = "TestPassword123!";
    String hash1 = HashFunction.hash(password);
    String hash2 = HashFunction.hash(password);
    
    // Hashes should be different due to salt
    assertNotEquals(hash1, hash2);
    
    // Both should verify
    assertTrue(HashFunction.verify(password, hash1));
    assertTrue(HashFunction.verify(password, hash2));
    
    // Wrong password should fail
    assertFalse(HashFunction.verify("WrongPassword", hash1));
}
```

### 2. Test Token Expiration
```java
@Test
public void testTokenExpiration() throws Exception {
    SuccessfulLoginDetails details = new SuccessfulLoginDetails(user);
    String token = JWTUtils.createJWT(details);
    
    // Token should be valid immediately
    JWTUtils.validateToken("Bearer " + token);
    
    // Create expired token manually
    String expiredToken = createExpiredToken();
    
    assertThrows(ExpiredJwtException.class, () -> {
        JWTUtils.validateToken("Bearer " + expiredToken);
    });
}
```

### 3. Test Authorization
```java
@Test
public void testUnauthorizedCouponCreation() {
    User customer = new User();
    customer.setUserType(UserType.CUSTOMER);
    String token = JWTUtils.createJWT(new SuccessfulLoginDetails(customer));
    
    Coupon coupon = new Coupon();
    
    assertThrows(ApplicationException.class, () -> {
        couponsController.createCoupon("Bearer " + token, coupon);
    });
}
```

---

## Deployment Checklist

- [ ] All environment variables configured
- [ ] JWT secret is strong (min 32 chars) and randomized
- [ ] Database credentials moved to environment
- [ ] SSL/TLS certificate configured
- [ ] CORS whitelist properly configured
- [ ] Error handling doesn't expose stack traces
- [ ] All endpoints require appropriate authorization
- [ ] Token expiration is validated
- [ ] Password hashing uses BCrypt
- [ ] Security headers are set
- [ ] Rate limiting configured
- [ ] Logging is properly configured
- [ ] Tests pass (create test suite first!)
- [ ] Code review completed

---

