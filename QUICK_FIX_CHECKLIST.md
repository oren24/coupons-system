# Quick Fix Checklist - Critical Issues

Use this checklist to track your fixes. Estimated completion: **12 hours**

---

## 🔴 CRITICAL FIXES (Fix First - Block Deployment)

### 1. ✓ Password Hashing - Replace SHA-256 with BCrypt
**File:** `backend/Coupons-Phase2/src/main/java/com/oren/coupons/encryptions/HashFunction.java`
**Time:** 2 hours
**Status:** ⬜ TODO

```
Steps:
1. [ ] Add dependency: spring-boot-starter-security to pom.xml
2. [ ] Replace HashFunction implementation with BCrypt
3. [ ] Update UserLogic.java login method
4. [ ] Update UserLogic.java addUser method
5. [ ] Test: Register new user, verify password hash is different each time
6. [ ] Test: Login with correct/incorrect password
7. [ ] Run mvn test
```

**Verification:**
```bash
mvn clean test -Dtest=PasswordHashTest
```

---

### 2. ✓ Move JWT Secret Out of Source Code
**File:** `backend/Coupons-Phase2/src/main/java/com/oren/coupons/consts/Consts.java`
**Time:** 1 hour
**Status:** ⬜ TODO

```
Steps:
1. [ ] Create .env file in project root (add to .gitignore)
2. [ ] Update Consts.java to read from environment
3. [ ] Set environment variable: export JWT_SECRET_KEY="..."
4. [ ] Update application.properties with placeholders
5. [ ] Test: Application starts with env var set
6. [ ] Test: Application fails gracefully without env var
7. [ ] Clean Git history: git filter-branch (or use BFG)
8. [ ] Update Git history: git push --force-with-lease
```

**Verification:**
```bash
export JWT_SECRET_KEY="test-secret-key-at-least-32-characters"
mvn spring-boot:run
# Should start successfully
```

---

### 3. ✓ Add Authorization to Modifying Endpoints
**Files:** 
- `backend/Coupons-Phase2/src/main/java/com/oren/coupons/controllers/CouponsController.java`
- `backend/Coupons-Phase2/src/main/java/com/oren/coupons/controllers/CompaniesController.java`

**Time:** 4 hours
**Status:** ⬜ TODO

```
Steps for CouponsController:
1. [ ] Add @RequestHeader("Authorization") to POST /coupons
2. [ ] Add authorization check: UserType must be COMPANY or ADMIN
3. [ ] Add company ownership check for COMPANY users
4. [ ] Add @RequestHeader("Authorization") to PUT /coupons
5. [ ] Add authorization check for PUT
6. [ ] Add @RequestHeader("Authorization") to DELETE /{id}
7. [ ] Add authorization check for DELETE
8. [ ] Rename deleteUser() method to deleteCoupon()
9. [ ] Test each endpoint with different user types
10. [ ] Test that CUSTOMER users get 401 errors

Steps for CompaniesController:
1. [ ] Add @RequestHeader("Authorization") to POST /companies
2. [ ] Add authorization check: Only ADMIN
3. [ ] Add @RequestHeader("Authorization") to PUT /companies
4. [ ] Add authorization check: Only ADMIN
5. [ ] Add @RequestHeader("Authorization") to DELETE /{id}
6. [ ] Add authorization check: Only ADMIN
7. [ ] Test each endpoint with different user types
```

**Verification:**
```bash
# Test as CUSTOMER (should fail)
curl -H "Authorization: Bearer <customer-token>" \
  -X POST http://localhost:8080/coupons

# Test as COMPANY (should succeed)
curl -H "Authorization: Bearer <company-token>" \
  -X POST http://localhost:8080/coupons
```

---

### 4. ✓ Fix Token Expiration Validation
**File:** `backend/Coupons-Phase2/src/main/java/com/oren/coupons/utils/JWTUtils.java`
**Time:** 1 hour
**Status:** ⬜ TODO

```
Steps:
1. [ ] Update decodeJWTClaims() to check expiration
2. [ ] Add ExpiredJwtException handling
3. [ ] Update validateToken() to properly throw on expiration
4. [ ] Update LoginFilter to handle ExpiredJwtException
5. [ ] Test: Create token, wait for expiration, verify rejection
6. [ ] Test: Normal token is accepted
```

**Verification:**
```bash
# Login (get token)
TOKEN=$(curl -X POST http://localhost:8080/users/login \
  -H "Content-Type: application/json" \
  -d '{"username":"test@test.com","password":"test123"}' | jq -r '.token')

# Use token immediately (should work)
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/coupons

# Wait > 1 minute, try again (should fail)
sleep 65
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/coupons
# Should return 401
```

---

### 5. ✓ Fix Login Response Format
**Files:**
- `backend/Coupons-Phase2/src/main/java/com/oren/coupons/controllers/UserController.java`
- `frontend/coupons-react/src/components/Login/Login.tsx`

**Time:** 1 hour
**Status:** ⬜ TODO

```
Backend Steps:
1. [ ] Update login() method to return ResponseEntity with Map
2. [ ] Response format: { "token": "eyJhbGc..." }
3. [ ] Test with curl to verify response format

Frontend Steps:
1. [ ] Update Login.tsx to parse response.data.token
2. [ ] Add error message display
3. [ ] Add input validation
4. [ ] Test login with valid/invalid credentials
```

**Verification:**
```bash
# Verify backend response
curl -X POST http://localhost:8080/users/login \
  -H "Content-Type: application/json" \
  -d '{"username":"test@test.com","password":"test123"}'

# Response should be: { "token": "..." }
# NOT: "eyJhbGc..." (plain string)
```

---

### 6. ✓ Add HTTP Security Headers
**File:** Create `backend/Coupons-Phase2/src/main/java/com/oren/coupons/filters/SecurityHeadersFilter.java`
**Time:** 1 hour
**Status:** ⬜ TODO

```
Steps:
1. [ ] Create new SecurityHeadersFilter class
2. [ ] Add headers: X-Content-Type-Options, X-Frame-Options, etc.
3. [ ] Register as @Component
4. [ ] Test: Make request, verify headers in response
```

**Verification:**
```bash
curl -v http://localhost:8080/coupons 2>&1 | grep -i "x-"
# Should show security headers
```

---

### 7. ✓ Fix Error Handling (Remove Stack Traces)
**File:** `backend/Coupons-Phase2/src/main/java/com/oren/coupons/exceptions/ExceptionsHandler.java`
**Time:** 2 hours
**Status:** ⬜ TODO

```
Steps:
1. [ ] Update ExceptionsHandler to return generic messages
2. [ ] Add proper HTTP status codes
3. [ ] Set logger for detailed logging (not console)
4. [ ] Remove printStackTrace() calls
5. [ ] Test: Cause error, verify no stack trace in response
6. [ ] Test: Check logs for full error details
```

**Verification:**
```bash
# Cause an error
curl -X POST http://localhost:8080/coupons \
  -H "Content-Type: application/json" \
  -d '{invalid json}'

# Response should be generic, no stack trace
```

---

## 📊 Progress Tracking

### Phase 1: Critical Security (12 hours total)

| # | Issue | Time | Status | Notes |
|---|-------|------|--------|-------|
| 1 | Password Hashing | 2h | ⬜ | Priority |
| 2 | JWT Secret | 1h | ⬜ | Priority |
| 3 | Authorization | 4h | ⬜ | Priority |
| 4 | Token Expiration | 1h | ⬜ | Priority |
| 5 | Login Response | 1h | ⬜ | Priority |
| 6 | Security Headers | 1h | ⬜ | Priority |
| 7 | Error Handling | 2h | ⬜ | Priority |
| **Total** | | **12h** | | |

### Additional Steps
- [ ] Code review (1h)
- [ ] Testing (2h)
- [ ] Documentation (1h)

---

## 🧪 Testing Checklist

After each fix, run:

```bash
# Build
mvn clean package

# Run application
mvn spring-boot:run

# Run tests
mvn test

# Test endpoints
curl -H "Authorization: Bearer <token>" http://localhost:8080/coupons

# Frontend
npm install
npm start
# Test login flow
```

---

## 🚀 Deployment Readiness

After completing all 7 critical fixes:

- [ ] All code changes committed
- [ ] Tests pass (>70% coverage minimum)
- [ ] No security warnings
- [ ] Environment variables documented
- [ ] Deployment guide updated
- [ ] Staging environment verified
- [ ] Security team approval obtained
- [ ] Go/No-go decision made

---

## 📝 Daily Progress Log

### Day 1
- [ ] Fix 1: Password Hashing (2h) - Start: ___ End: ___
- [ ] Fix 2: JWT Secret (1h) - Start: ___ End: ___
- **Daily Total:** ___ hours

### Day 2
- [ ] Fix 3: Authorization (4h) - Start: ___ End: ___
- **Daily Total:** ___ hours

### Day 3
- [ ] Fix 4: Token Expiration (1h) - Start: ___ End: ___
- [ ] Fix 5: Login Response (1h) - Start: ___ End: ___
- [ ] Fix 6: Security Headers (1h) - Start: ___ End: ___
- **Daily Total:** ___ hours

### Day 4
- [ ] Fix 7: Error Handling (2h) - Start: ___ End: ___
- [ ] Testing & Verification (2h) - Start: ___ End: ___
- [ ] Final Review (1h) - Start: ___ End: ___
- **Daily Total:** ___ hours

---

## ⚠️ High-Risk Items

Watch out for these while fixing:

- [ ] JWT library version compatibility
- [ ] Breaking changes in password migration
- [ ] Token refresh flow complexity
- [ ] Database transaction issues
- [ ] CORS whitelist validation
- [ ] Environment variable defaults

---

## 🔄 Rollback Plan

If something breaks:

1. [ ] Revert last commit: `git revert HEAD`
2. [ ] Verify previous version works
3. [ ] Identify issue
4. [ ] Create fix branch
5. [ ] Test thoroughly before merging

---

## 📞 Support Resources

- Spring Security Docs: https://spring.io/projects/spring-security
- JWT Best Practices: https://auth0.com/blog/critical-vulnerabilities-in-json-web-token-libraries/
- OWASP Top 10: https://owasp.org/www-project-top-ten/
- Spring Boot Security: https://spring.io/guides/gs/securing-web/

---

## ✅ Final Checklist

Before declaring "DONE":

- [ ] All 7 critical fixes implemented
- [ ] No compiler errors
- [ ] No security warnings
- [ ] All tests passing
- [ ] Code review approved
- [ ] Documentation updated
- [ ] Tested in staging
- [ ] Security team verified
- [ ] Ready for production deployment

---

**Expected Completion Date:** _______________

**Actual Completion Date:** _______________

**Completed By:** _______________

**Review By:** _______________

**Approved By:** _______________

---

*This checklist is a guide. Adjust timeline based on your team's capacity.*
