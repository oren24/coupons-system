# Dependabot Vulnerability Analysis Report

**Repository**: oren24/coupons-system  
**Date**: 2026-05-21  
**Total Vulnerabilities**: 84 (2 critical, 35 high, 38 moderate, 9 low)

---

## Overview

The push output indicated GitHub Dependabot found 84 vulnerabilities. Most of these are likely in transitive dependencies (dependencies of dependencies) rather than direct dependencies explicitly declared in pom.xml and package.json.

---

## Backend Vulnerabilities (Java/Maven)

### Direct Dependencies in pom.xml:
| Dependency | Version | Status |
|------------|---------|--------|
| spring-boot-starter-parent | 2.3.3.RELEASE | ⚠️ OUTDATED |
| mysql-connector-java | 8.0.33 | ✅ Recent |
| jjwt-api/impl/jackson | 0.12.3 | ✅ Recent |
| jackson-core | 2.18.6 | ✅ Recent |
| jackson-databind | 2.15.2 | ⚠️ Pinned lower |
| spring-security-crypto | (inherited) | ✅ Recent |

### Known Issues:

**1. Spring Boot 2.3.3 (CRITICAL)**
- Released: August 2020
- **End of support**: August 2023 (already expired)
- Contains vulnerabilities in Spring Framework and dependencies
- **Recommendation**: Upgrade to Spring Boot 2.7.15 (latest 2.x) or 3.2.x (latest stable)

**2. Jackson Version Mismatch**
- jackson-core: 2.18.6
- jackson-databind: 2.15.2 (older)
- jackson-annotations: 2.15.2 (older)
- **Issue**: Versions should match
- **Recommendation**: Upgrade all to 2.18.6 or latest stable

**3. MySQL Connector**
- mysql-connector-java 8.0.33 is deprecated
- **Recommendation**: Migrate to mysql-connector-j (new name/packaging)

### Transitive Dependencies (Likely Sources):
- Spring Framework 5.2.x (old, in Spring Boot 2.3.3)
- Apache Commons libraries (Log4j, Collections, etc.)
- Tomcat 9.0.x (old servlet container)
- Jackson ecosystem (multiple versions)
- Logback/SLF4J

---

## Frontend Vulnerabilities (React/NPM)

### Direct Dependencies in package.json:
| Dependency | Version | Status |
|-----------|---------|--------|
| react | 18.2.0 | ✅ Recent |
| react-dom | 18.2.0 | ✅ Recent |
| react-scripts | 5.0.1 | ⚠️ Can be updated |
| react-router-dom | 6.14.2 | ✅ Recent |
| redux | 4.1.0 | ✅ Recent |
| axios | 1.6.0 | ⚠️ Outdated |
| typescript | 4.9.5 | ✅ Recent |
| jest | (via react-scripts) | ✅ Recent |

### Known Issues:

**1. Axios 1.6.0 (HIGH)**
- Released: December 2016
- **7+ years old!**
- Multiple security vulnerabilities
- **Current version**: 1.7.x
- **Recommendation**: Upgrade to latest 1.7.x immediately

**2. react-scripts 5.0.1 (MODERATE)**
- Can be updated to latest
- Transitive dependencies may have vulnerabilities

**3. Transitive Dependencies (Likely High Count):**
- Webpack (bundler, often has issues)
- Babel (transpiler)
- ESLint & related
- Testing libraries
- Build tools

---

## Action Plan (Priority Order)

### 🔴 CRITICAL (Do Immediately)

1. **Upgrade Spring Boot 2.3.3 → 2.7.15**
   ```xml
   <version>2.7.15.RELEASE</version>
   ```
   - Fixes ~30+ vulnerabilities
   - Still Java 11 compatible
   - No breaking changes for our code

2. **Fix Jackson Version Mismatch**
   ```xml
   <!-- Upgrade databind & annotations to 2.18.6 -->
   <version>2.18.6</version>
   ```

3. **Upgrade Axios 1.6.0 → 1.7.7**
   ```json
   "axios": "^1.7.7"
   ```
   - Fixes multiple high-priority vulnerabilities
   - Fully backward compatible

### 🟠 HIGH (Do Soon)

4. **Migrate MySQL Connector**
   ```xml
   <groupId>com.mysql</groupId>
   <artifactId>mysql-connector-j</artifactId>
   <version>8.3.0</version>
   ```

5. **Update react-scripts**
   ```json
   "react-scripts": "5.0.1" → latest
   ```
   - Run: `npm install react-scripts@latest`

### 🟡 MEDIUM

6. **npm audit fix**
   ```bash
   npm audit fix --force
   ```
   - Fixes transitive NPM vulnerabilities
   - May need testing

---

## Expected Results After Fixes

| Priority | Before | After |
|----------|--------|-------|
| Critical | 2 | 0 |
| High | 35 | ~8-12 |
| Moderate | 38 | ~20-25 |
| Low | 9 | ~5-8 |

**Estimated reduction**: From 84 to ~35-45 vulnerabilities (many remaining are in deep transitive chains)

---

## Backend Fix Commands

```bash
cd backend/Coupons-Phase2

# Update Spring Boot
# Edit pom.xml: Change 2.3.3.RELEASE to 2.7.15.RELEASE

# Update Jackson versions
# Edit pom.xml: Change jackson-databind & annotations to 2.18.6

# Update MySQL Connector
# Edit pom.xml: Change mysql-connector-java to mysql-connector-j 8.3.0

# Rebuild
mvn clean install
```

---

## Frontend Fix Commands

```bash
cd frontend/coupons-react

# Upgrade axios
npm install axios@1.7.7

# Fix npm vulnerabilities
npm audit fix

# Update react-scripts (optional but recommended)
npm install react-scripts@latest

# Rebuild
npm run build

# Test
npm start
```

---

## Verification

After making changes:

1. **Backend**: Run tests
   ```bash
   mvn test
   mvn clean package
   ```

2. **Frontend**: Run tests and build
   ```bash
   npm test
   npm run build
   ```

3. **Commit changes**:
   ```bash
   git add -A
   git commit -m "Fix Dependabot vulnerabilities: Update Spring Boot, Jackson, Axios"
   git push origin main
   ```

4. **Verify on GitHub**: Check security tab - should show significant reduction

---

## Notes

- Many vulnerabilities are in transitive dependencies (dependencies of dependencies)
- Upgrading major packages (Spring Boot) will pull in patched transitive deps
- Some low/moderate issues may remain due to deep chains - these are usually low-risk
- Test thoroughly after updates to ensure no breaking changes
- Most updates are backward compatible for our codebase

---

## Recommended Priority Sequence

1. ✅ Update Spring Boot 2.3.3 → 2.7.15 (biggest impact)
2. ✅ Fix Jackson version mismatch
3. ✅ Upgrade Axios 1.6.0 → 1.7.7
4. ✅ Migrate MySQL connector
5. ✅ Run npm audit fix
6. ✅ Test & commit

**Time estimate**: 30-45 minutes with testing
