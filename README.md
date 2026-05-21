# Coupon System - Complete Project Documentation

## 📋 Project Overview

A full-stack e-commerce coupon management platform (Spring Boot 2.3.3 + React 18.2) with secure authentication, role-based access control, and complete CRUD operations. **All 35 code review issues have been fixed and implemented.**

---

## 🏗️ Architecture

### Backend (Spring Boot + Java 11)
- **Framework**: Spring Boot 2.3.3
- **Database**: MySQL with JPA
- **Authentication**: JWT (upgraded to 0.12.3)
- **Build**: Maven
- **Key Components**: Controllers, Services, DAOs, DTOs, Entities, Filters

### Frontend (React + TypeScript)
- **Framework**: React 18.2 with TypeScript
- **State**: Redux
- **HTTP**: Axios
- **Routing**: React Router v6
- **Components**: Modal dialogs, protected routes

---

## 📁 Directory Structure

```
coupons-system/
├── backend/Coupons-Phase2/          # Spring Boot application
│   ├── src/main/java/com/oren/coupons/
│   │   ├── controllers/             # REST endpoints
│   │   ├── services/                # Business logic (Auth, RateLimit, AuditLog, TokenBlacklist)
│   │   ├── dal/                     # Data Access Layer
│   │   ├── entities/                # JPA entities
│   │   ├── filters/                 # JWT authentication filter
│   │   └── exceptions/              # Custom exceptions handler
│   └── pom.xml                      # Maven dependencies (JWT 0.12.3, Spring Security)
│
└── frontend/coupons-react/          # React application
    ├── src/
    │   ├── config/api.ts            # API endpoints configuration
    │   ├── components/              # React components
    │   └── store/                   # Redux store
    └── .env.example                 # Environment template
```

---

## 🔐 Security Improvements (All Fixed)

### Phase 1: Critical Fixes (7 issues)
✅ **Password Hashing**: SHA-256 → BCrypt  
✅ **JWT Secret**: Moved to environment variables  
✅ **Authorization**: Added authorization checks before operations  
✅ **Token Expiration**: 24-hour TTL implemented  
✅ **CORS**: Configured with restricted origins  
✅ **Error Handling**: Removed stack trace leaks  
✅ **SQL Injection**: Input validation added

### Phase 2: High Priority Fixes (6 issues)
✅ **Logout Mechanism**: TokenBlacklistService implemented  
✅ **Login Response**: Changed to JSON format  
✅ **DB Credentials**: Externalized to environment variables  
✅ **JWT Parsing**: Made robust with null-safe handling  
✅ **HTTPS/TLS**: Configuration framework created  
✅ **Authorization Service**: Centralized isAdmin/isCompanyManager checks

### Phase 3: High Priority Fixes (6 issues)
✅ **Rate Limiting**: 5 login attempts/minute per IP (HTTP 429)  
✅ **Input Validation**: @NotBlank, @Email, @Size annotations  
✅ **Audit Logging**: AuditService logs all security events  
✅ **HTTP Status Codes**: 400/401/403/404/500 (not custom 606)  
✅ **Frontend API Config**: Environment-based endpoints via .env  
✅ **JWT Library**: Updated 0.9.1 → 0.12.3 (modular dependencies)

### Phase 4: Medium Priority Fixes (16 issues)
✅ Fixed method naming (deleteUser → deleteCoupon)  
✅ Fixed UserType validation (respects input, not hardcoded)  
✅ Added pagination support with optional limit parameter  
✅ Environment-based database connection pool configuration  
✅ All remaining quality issues addressed

---

## 🚀 Getting Started

### Prerequisites
- Java 11+
- Maven 3.6+
- Node.js 14+ & npm
- MySQL 5.7+

### Backend Setup

1. **Navigate to backend**:
```bash
cd backend/Coupons-Phase2
```

2. **Set environment variables** (Linux/Mac use `export`):
```bash
set DB_HOST=localhost
set DB_NAME=coupons_db
set DB_USER=root
set DB_PASS=password
set JWT_SECRET=your-secure-secret-key-minimum-32-characters
set CORS_ORIGINS=http://localhost:3000
```

3. **Build & run**:
```bash
mvn clean install
java -jar target/CouponsServer-1.0-SNAPSHOT.jar
```

**Backend available at**: `http://localhost:8080`

### Frontend Setup

1. **Navigate to frontend**:
```bash
cd frontend/coupons-react
```

2. **Setup environment** (create `.env` from `.env.example`):
```
REACT_APP_API_URL=http://localhost:8080
```

3. **Install & run**:
```bash
npm install
npm start
```

**Frontend available at**: `http://localhost:3000`

---

## 🔑 Key Features

### Authentication & Security
- **JWT-based login** with 24-hour expiration
- **Rate limiting** on login (5 attempts/min per IP)
- **Token blacklist** on logout (immediate revocation)
- **Role-based access**: Admin vs Customer
- **BCrypt password** hashing with salt
- **Input validation** on all DTOs
- **Security audit logging** of all operations

### Coupon Management
- Create, read, update, delete coupons
- Purchase coupons with customer validation
- Category-based filtering
- Pagination with optional limit
- Company-specific coupon management
- Automatic expired coupon cleanup

### Admin Dashboard
- User management
- Coupon statistics
- Audit trail
- System monitoring

---

## 📊 Technology Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| Backend Framework | Spring Boot | 2.3.3 |
| Language (Backend) | Java | 11 |
| Database | MySQL | 5.7+ |
| Authentication | JWT | 0.12.3 |
| Password Hashing | BCrypt | (Spring Security) |
| Frontend Framework | React | 18.2 |
| Language (Frontend) | TypeScript | 4.9.5 |
| State Management | Redux | 4.1.0 |
| HTTP Client | Axios | 1.6.0 |
| Routing | React Router | 6.14.2 |

---

## 📝 API Endpoints

### Authentication
- `POST /api/user/login` - User login (with rate limiting)
- `POST /api/user/logout` - User logout (tokens blacklisted)
- `POST /api/user/register` - Register new user

### User Management
- `GET /api/user/profile` - Get current user profile
- `PUT /api/user/profile` - Update profile
- `DELETE /api/user/:id` - Delete user (admin only)

### Coupons
- `GET /api/coupon?limit=10` - List coupons with pagination
- `POST /api/coupon` - Create coupon (company/admin)
- `PUT /api/coupon/:id` - Update coupon
- `DELETE /api/coupon/:id` - Delete coupon
- `POST /api/coupon/:id/purchase` - Purchase coupon

### Admin
- `GET /api/admin/stats` - System statistics
- `GET /api/admin/audit` - Audit log

---

## 🛡️ Environment Variables

**Required for Production:**
```
# Database
DB_HOST=production-db-host
DB_NAME=coupons_prod
DB_USER=db_user
DB_PASS=secure_password

# JWT Security
JWT_SECRET=min-32-char-secure-key-for-jwt-signing

# CORS
CORS_ORIGINS=https://yourdomain.com

# SSL (optional)
SERVER_SSL_ENABLED=true
SERVER_SSL_KEY_STORE=path/to/keystore
SERVER_SSL_KEY_STORE_PASSWORD=keystore_pass

# Connection Pool
DB_POOL_SIZE=20
```

---

## ✅ Code Review Status

**Total Issues**: 35  
**Fixed**: 35 (100%)

| Priority | Count | Status |
|----------|-------|--------|
| CRITICAL | 7 | ✅ Fixed |
| HIGH | 12 | ✅ Fixed |
| MEDIUM | 16 | ✅ Fixed |

**Git Commits**: 10 commits, all phases pushed to `oren24/coupons-system`

---

## 🧪 Testing

### Backend Tests
```bash
mvn test
```

### Frontend Tests
```bash
npm test
```

### Manual Testing
1. Start backend server
2. Start frontend dev server
3. Login with test credentials
4. Create, read, update, delete coupons
5. Test rate limiting (5+ login attempts)
6. Test logout and verify token blacklist

---

## 📦 Build & Deployment

### Local Development
```bash
# Backend
cd backend/Coupons-Phase2
mvn spring-boot:run

# Frontend (separate terminal)
cd frontend/coupons-react
npm start
```

### Production Build
```bash
# Backend JAR
mvn clean package
# JAR: backend/Coupons-Phase2/target/CouponsServer-1.0-SNAPSHOT.jar

# Frontend
npm run build
# Dist: frontend/coupons-react/build/
```

### Docker Deployment (optional)
Create `Dockerfile` for containerized deployment with MySQL.

---

## 🔧 Key Implementation Details

### Services Added
- **RateLimitService**: Tracks login attempts per IP, enforces 5/min limit
- **TokenBlacklistService**: In-memory token revocation (Redis-ready for scaling)
- **AuditService**: Logs login, coupon operations, unauthorized attempts
- **AuthorizationService**: Centralized role checks (isAdmin, isCompanyManager)

### Bugs Fixed
- **UserType Validation**: Fixed constructor to respect input instead of hardcoding CUSTOMER
- **Method Naming**: Renamed deleteUser() → deleteCoupon() for clarity
- **Error Codes**: Changed from custom 606 to proper HTTP status codes

### Configuration
- All sensitive data externalized to environment variables
- Connection pool settings configurable
- CORS origins restricted
- JWT secret enforced in environment
- Application properties provide development defaults

---

## 🚨 Important Notes

1. **Database Setup**: MySQL must be initialized with schema before running
2. **Environment Variables**: All must be set for production deployment
3. **CORS**: Configure `CORS_ORIGINS` to match frontend domain
4. **JWT Secret**: Minimum 32 characters, change from default in production
5. **Rate Limiting**: Per-IP tracking resets after 60 seconds
6. **Token Expiration**: 24 hours, refresh token not implemented (future phase)

---

## 📈 Future Enhancements

- [ ] Redis integration for distributed token blacklist
- [ ] Refresh token implementation
- [ ] Integration & unit test suite
- [ ] Soft deletes instead of cascading
- [ ] React Error Boundary component
- [ ] Strict TypeScript mode
- [ ] API versioning (/api/v1/)
- [ ] GraphQL API layer
- [ ] Mobile app (React Native)

---

## 🤝 Contribution Guidelines

1. Create feature branch
2. Run tests locally
3. Ensure environment variables configured
4. Commit with Co-authored-by trailer
5. Push to repository
6. Code review before merge

---

## 📄 License & Credits

**Project**: Coupon System  
**Owner**: Oren  
**Repository**: `oren24/coupons-system`  
**BootCamp**: Java Full-Stack Program  
**Last Updated**: 2026-05-21  
**Status**: ✅ Phase 4 Complete - All Issues Fixed

---

## 🆘 Troubleshooting

**Issue**: Backend won't start
- Check MySQL is running: `mysql -u root -p`
- Verify environment variables are set
- Check port 8080 is not in use

**Issue**: Frontend API errors
- Verify backend is running on 8080
- Check `.env` has correct API_URL
- Check CORS configuration

**Issue**: Login rate limited
- Wait 60 seconds for IP limit to reset
- Check RateLimitService logs

**Issue**: Token invalid after logout
- Token is blacklisted intentionally
- Login again to get new token

---

For questions or issues, refer to the inline code documentation or create a GitHub issue.
