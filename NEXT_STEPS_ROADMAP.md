# 📋 Next Steps - Phase 2 & 3 Roadmap

**Current Status:** Phase 1 (7 Critical Fixes) ✅ COMPLETE  
**Next:** Phase 2 (12 High Priority Issues) → Phase 3 (16 Medium Priority Issues)

---

## 🎯 Overview

From the comprehensive code review, there are **28 remaining issues** to address:

| Phase | Priority | Count | Estimated Time |
|-------|----------|-------|-----------------|
| ✅ Phase 1 | CRITICAL | 7 | 12 hours (DONE) |
| 📋 Phase 2 | HIGH | 12 | 14-16 hours |
| 📋 Phase 3 | MEDIUM | 16 | 20-24 hours |
| **Total Remaining** | - | **28** | **34-40 hours** |

---

## 🟠 PHASE 2: HIGH PRIORITY ISSUES (Next)

### Issue #1: Missing Authorization Checks on Endpoints
**Severity:** HIGH - Privilege escalation  
**Estimated Time:** 2 hours  
**Files:**
- `CouponsController.java` - POST, PUT endpoints
- `CompaniesController.java` - POST, PUT endpoints
- `CategoryController.java` - All modifying endpoints

**Problem:**
- Coupon creation/modification endpoints have no authorization
- Any authenticated user can modify any company's coupons
- No verification that user belongs to company

**Fix:**
```java
@PreAuthorize("hasRole('ADMIN') or @companyService.isUserCompanyManager(authentication.principal.userId, #couponDto.companyId)")
@PostMapping
public void createCoupon(@RequestBody Coupon coupon) { }
```

**Tests Needed:**
- [ ] Customer cannot create coupon
- [ ] Company manager can only create for own company
- [ ] Admin can create for any company

---

### Issue #2: Incomplete Logout Mechanism  
**Severity:** HIGH - Session security  
**Estimated Time:** 1.5 hours  
**Files:**
- `UserController.java` - Add logout endpoint
- `LoginFilter.java` - Add token blacklist logic
- Create: `TokenBlacklistService.java`

**Problem:**
- No logout endpoint in backend
- No token invalidation (token valid until expiration)
- User can use old token after logout

**Fix:**
```java
@PostMapping("/logout")
public void logout(@RequestHeader("Authorization") String token) {
    tokenBlacklistService.addToBlacklist(token);
}

// In LoginFilter:
if (tokenBlacklistService.isBlacklisted(token)) {
    response.setStatus(401);
    return;
}
```

**Tasks:**
- [ ] Create TokenBlacklistService with Redis or in-memory cache
- [ ] Add logout endpoint to UserController
- [ ] Update LoginFilter to check blacklist
- [ ] Test logout invalidates token

---

### Issue #3: Frontend Login Response Mismatch
**Severity:** HIGH - Authentication broken  
**Estimated Time:** 1 hour  
**Files:**
- `UserController.java` (Backend)
- `LoginRegister.tsx` (Frontend)

**Problem:**
```java
// Backend returns plain string
return token;  // "eyJhbGciOi..."

// Frontend expects JSON
let token = response.data.data;  // undefined!
```

**Fix - Backend:**
```java
@PostMapping("/login")
public ResponseEntity<Map<String, Object>> login(@RequestBody User user) throws ApplicationException {
    String token = userLogic.login(user.getUsername(), user.getPassword());
    return ResponseEntity.ok(Map.of(
        "success", true,
        "token", token,
        "user", user
    ));
}
```

**Fix - Frontend:**
```typescript
const response = await axios.post(`${API_URL}/users/login`, {username, password});
const token = response.data.token;
localStorage.setItem('token', token);
```

**Tests:**
- [ ] Login returns proper JSON structure
- [ ] Frontend correctly parses token
- [ ] Token stored in localStorage

---

### Issue #4: Database Credentials Exposed
**Severity:** HIGH - Data breach  
**Estimated Time:** 0.5 hours  
**Files:** `application.properties`

**Current:**
```properties
spring.datasource.username=root
spring.datasource.password=1234
```

**Fix:**
```properties
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.url=${DB_URL}
```

**Tasks:**
- [ ] Update application.properties to use environment variables
- [ ] Document required environment variables
- [ ] Remove hardcoded credentials from repository

---

### Issue #5: No HTTPS/TLS Configuration
**Severity:** HIGH - Data in transit  
**Estimated Time:** 1.5 hours

**Fix:**
```properties
# SSL/TLS Configuration
server.ssl.enabled=true
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=${SSL_PASSWORD}
server.ssl.key-store-type=PKCS12
server.ssl.key-alias=tomcat
server.port=8443
```

**Tasks:**
- [ ] Generate SSL certificate
- [ ] Update application.properties
- [ ] Configure HTTPS on production
- [ ] Update frontend to use HTTPS

---

### Issue #6: Deprecated JWT Token Parsing
**Severity:** HIGH - Runtime errors  
**Estimated Time:** 0.5 hours  
**File:** `JWTUtils.java`

**Current (Fragile):**
```java
String[] textSegments = token.split(" ");
String tokenWithoutBearer = textSegments[1];  // ArrayIndexOutOfBoundsException if malformed
```

**Fix:**
```java
private static String getTokenWithoutBearer(String token) {
    if (token != null && token.startsWith("Bearer ")) {
        return token.substring(7);
    }
    return token;
}
```

---

### Issue #7: Manual JWT Decoding (Frontend)
**Severity:** HIGH - Fragile code  
**Estimated Time:** 0.5 hours  
**File:** `LoginRegister.tsx`

**Current:**
```typescript
let decodedToken = atob(tokenBody);
let user = JSON.parse(decodedToken).sub;  // Assumes .sub exists
```

**Fix:**
```typescript
import jwt_decode from 'jwt-decode';

const decodeToken = (token: string) => {
    try {
        const decoded = jwt_decode(token) as any;
        return decoded.sub || null;
    } catch (error) {
        console.error('Invalid token:', error);
        return null;
    }
};
```

---

### Issue #8: No Request Rate Limiting
**Severity:** HIGH - DoS vulnerability  
**Estimated Time:** 2 hours  
**Files:** All controllers

**Fix:**
```xml
<!-- pom.xml -->
<dependency>
    <groupId>com.github.vladimir-bukhtoyarov</groupId>
    <artifactId>bucket4j-core</artifactId>
    <version>7.6.0</version>
</dependency>
```

```java
@Component
public class RateLimitInterceptor implements HandlerInterceptor {
    private final Bucket bucket = Bucket4j.builder()
        .addLimit(Limit.of(100, Refill.intervally(100, Duration.ofMinutes(1))))
        .build();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (bucket.tryConsume(1)) {
            return true;
        }
        response.setStatus(429); // Too Many Requests
        return false;
    }
}
```

---

### Issue #9: No Input Sanitization (XSS/Injection)
**Severity:** HIGH - XSS attacks  
**Estimated Time:** 1.5 hours  
**Files:** DTOs, request validators

**Fix:**
```java
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

@Component
public class InputSanitizer {
    private static final PolicyFactory HTML_POLICY = Sanitizers.FORMATTING;

    public String sanitize(String input) {
        if (input == null) return null;
        return HTML_POLICY.sanitize(input);
    }
}

// In DTOs:
public class Coupon {
    @PreUpdate
    public void sanitize() {
        this.description = sanitizer.sanitize(description);
        this.name = sanitizer.sanitize(name);
    }
}
```

---

### Issue #10: No Comprehensive Logging
**Severity:** HIGH - Debugging, audit trail  
**Estimated Time:** 2 hours

**Fix:**
```xml
<!-- pom.xml -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-logging</artifactId>
</dependency>
```

```java
@Aspect
@Component
public class AuditLoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(AuditLoggingAspect.class);

    @Around("@annotation(Auditable)")
    public Object audit(ProceedingJoinPoint pjp) throws Throwable {
        logger.info("Audit: {} called by {}", pjp.getSignature(), getCurrentUser());
        return pjp.proceed();
    }
}
```

---

### Issue #11: Missing Error HTTP Status Codes
**Severity:** HIGH - API contract  
**Estimated Time:** 1 hour

**Current Issue:**
- All errors return 606 with generic message
- Client can't distinguish error types

**Fix:**
```java
@ExceptionHandler(ApplicationException.class)
public ResponseEntity<ErrorBean> handleApplicationException(ApplicationException ex) {
    HttpStatus status = switch(ex.getErrorType()) {
        case NOT_FOUND -> HttpStatus.NOT_FOUND;
        case UNAUTHORIZED -> HttpStatus.UNAUTHORIZED;
        case FORBIDDEN -> HttpStatus.FORBIDDEN;
        case VALIDATION_ERROR -> HttpStatus.BAD_REQUEST;
        default -> HttpStatus.INTERNAL_SERVER_ERROR;
    };
    return new ResponseEntity<>(errorBean, status);
}
```

---

### Issue #12: Hardcoded API URLs (Frontend)
**Severity:** HIGH - Deployment issues  
**Estimated Time:** 1 hour  
**Files:** All service files

**Current:**
```typescript
const API_URL = "http://localhost:8080";
```

**Fix:**
```typescript
// .env
REACT_APP_API_URL=http://localhost:8080

// In code
const API_URL = process.env.REACT_APP_API_URL || "http://localhost:8080";
```

---

## 🟡 PHASE 3: MEDIUM PRIORITY ISSUES (After Phase 2)

### Issue #13-28: Medium Priority Items
**Total Time:** 20-24 hours

**List:**
13. Code duplication in DAL repositories
14. No pagination on list endpoints
15. No caching layer
16. Frontend state management could use Redux middleware
17. No integration tests
18. Missing JSDoc/JavaDoc
19. Inconsistent error messages
20. No API versioning strategy
21. Missing CORS preflight handling
22. Weak password validation requirements
23. No email verification on registration
24. Missing coupon expiration logic (frontend)
25. No transaction management for multi-step operations
26. Inconsistent date formats
27. No monitoring/metrics collection
28. Missing security headers (CSP, STS, etc.)

---

## 📊 Phase 2 Implementation Plan

### Week 1 Summary
```
Day 1 (2 hrs):
  ├─ Issue #1: Add authorization checks
  └─ Issue #2: Implement logout mechanism

Day 2 (2 hrs):
  ├─ Issue #3: Fix login response format
  └─ Issue #4: Externalize DB credentials

Day 3 (2 hrs):
  ├─ Issue #5: Configure HTTPS/TLS
  └─ Issue #6: Fix JWT token parsing

Day 4 (2 hrs):
  ├─ Issue #7: Fix frontend JWT decoding
  └─ Issue #8: Add rate limiting

Day 5 (2 hrs):
  ├─ Issue #9: Input sanitization
  ├─ Issue #10: Comprehensive logging
  ├─ Issue #11: HTTP status codes
  └─ Issue #12: Environment variables (frontend)

Testing & Review (3 hrs):
  ├─ Integration testing
  ├─ Security testing
  ├─ Deployment testing
  └─ Documentation review
```

---

## ✅ Phase 2 Checklist

- [ ] Issue #1: Authorization checks on endpoints
- [ ] Issue #2: Logout mechanism with token blacklist
- [ ] Issue #3: Login response JSON format
- [ ] Issue #4: Database credentials externalized
- [ ] Issue #5: HTTPS/TLS configured
- [ ] Issue #6: JWT token parsing fixed
- [ ] Issue #7: Frontend JWT decoding improved
- [ ] Issue #8: Rate limiting configured
- [ ] Issue #9: Input sanitization implemented
- [ ] Issue #10: Logging configured
- [ ] Issue #11: HTTP status codes correct
- [ ] Issue #12: API URLs environment-based
- [ ] All Phase 2 tests passing
- [ ] Security review completed
- [ ] Documentation updated

---

## 🚀 Decision Point: Which Phase Next?

### Recommended Path:
1. ✅ **Phase 1 (DONE):** Critical security fixes
2. 📋 **Phase 2 (RECOMMENDED NEXT):** High priority improvements
3. 📋 **Phase 3:** Medium priority enhancements

### Alternative: Skip to Phase 3?
- Only if Phase 2 issues are working in current deployment
- Not recommended - Phase 2 has security implications

---

## 📈 Expected Outcome After Phase 2

**Security:** 🟢 Strong (with Phase 1)  
**Stability:** 🟡 Improved (authorization, logging)  
**Maintainability:** 🟡 Better (cleaner code)  
**Production Readiness:** 🟡 Approaching

---

## 💡 Next Action

### Immediate (Today):
1. Review this roadmap with team
2. Prioritize Phase 2 issues
3. Assign team members to issues
4. Create GitHub issues for each item

### This Week:
1. Start Issue #1: Authorization checks
2. Start Issue #2: Logout mechanism
3. Set up testing environment

### By End of Week:
- [ ] First 3 issues completed
- [ ] Integration tests passing
- [ ] Security review passed

---

## 📚 Resources

- **Spring Security:** https://spring.io/projects/spring-security
- **OWASP Top 10:** https://owasp.org/www-project-top-ten/
- **JWT Best Practices:** https://tools.ietf.org/html/rfc8725
- **Rate Limiting:** https://bucket4j.com/

---

## 🎯 Questions for Team

1. **Priority:** Should we do all of Phase 2 before Phase 3?
2. **Resources:** How many developers can work on this?
3. **Timeline:** When should Phase 2 be complete?
4. **Testing:** How extensive should testing be?
5. **Security:** Should we do external security audit?

---

**Status:** 🟢 Phase 1 Complete → Ready for Phase 2  
**Recommendation:** Start with Issue #1 (Authorization checks) this week  
**Estimated Completion:** 2 weeks (Phase 2 + Phase 3)

---

Generated: 2026-05-21  
Based on: CODE_REVIEW.md (Comprehensive Code Review)  
Phase: Post-Critical Fixes Planning
