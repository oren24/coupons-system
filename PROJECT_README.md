# Coupon System - Full Stack Application

A comprehensive full-stack web application for managing and distributing coupons, built with modern technologies and best practices.

## 📋 Project Overview

The Coupon System is a complete e-commerce coupon management platform that allows administrators and customers to create, manage, browse, and purchase coupons. The application features a robust backend API with secure authentication and a responsive React frontend.

## 🏗️ Architecture

### Backend Structure
- **Framework**: Spring Boot 2.3.3
- **Language**: Java 11
- **Database**: MySQL
- **Authentication**: JWT (JSON Web Tokens)
- **Build Tool**: Maven

### Frontend Structure
- **Framework**: React 18.2
- **Language**: TypeScript
- **State Management**: Redux
- **HTTP Client**: Axios
- **Routing**: React Router v6
- **UI Components**: React Modal

## 📁 Directory Structure

```
coupons-system/
├── backend/
│   └── Coupons-Phase2/                    # Spring Boot backend application
│       ├── src/
│       │   ├── main/java/com/oren/coupons/
│       │   │   ├── beans/                 # Configuration beans
│       │   │   ├── controllers/           # REST API endpoints
│       │   │   ├── dal/                   # Data Access Layer (DAO)
│       │   │   ├── dto/                   # Data Transfer Objects
│       │   │   ├── entities/              # JPA entities
│       │   │   ├── enums/                 # Enumerations
│       │   │   ├── exceptions/            # Custom exceptions
│       │   │   ├── filters/               # Request filters
│       │   │   ├── logic/                 # Business logic layer
│       │   │   ├── consts/                # Constants
│       │   │   ├── encryptions/           # Encryption utilities
│       │   │   ├── threads/               # Threading utilities
│       │   │   ├── TimerTasks/            # Scheduled tasks
│       │   │   ├── utils/                 # Utility functions
│       │   │   └── Main.java              # Application entry point
│       │   └── test/                      # Unit and integration tests
│       ├── pom.xml                        # Maven configuration
│       └── target/                        # Compiled output
│
├── frontend/
│   └── coupons-react/                     # React frontend application
│       ├── src/                           # React components and logic
│       ├── public/                        # Static assets
│       ├── package.json                   # NPM dependencies
│       ├── tsconfig.json                  # TypeScript configuration
│       ├── .gitignore
│       └── README.md
```

## 🔑 Key Features

### Backend
- **User Authentication**: JWT-based secure authentication
- **Role-Based Access Control**: Admin and customer roles
- **Coupon Management**: Create, read, update, delete coupons
- **Database Persistence**: MySQL for reliable data storage
- **RESTful API**: Standardized REST endpoints
- **Exception Handling**: Custom exception handling with filters
- **Scheduled Tasks**: Timer tasks for background operations
- **Encryption**: Secure password and sensitive data encryption

### Frontend
- **Responsive UI**: Modern React components
- **State Management**: Redux for centralized state management
- **Secure Routing**: Protected routes based on user roles
- **HTTP Requests**: Axios for API communication
- **Modal Dialogs**: User-friendly modal windows for actions
- **Type Safety**: TypeScript for compile-time type checking
- **Form Handling**: Validated form inputs

## 🛠️ Technology Stack

### Backend Dependencies
| Technology | Version | Purpose |
|-------------|---------|---------|
| Spring Boot | 2.3.3 | Web framework |
| MySQL Connector | 8.0.33 | Database driver |
| Spring Data JPA | Latest | ORM for database operations |
| JWT | 0.9.1 | Token-based authentication |
| Jackson | 2.18.6 | JSON processing |

### Frontend Dependencies
| Technology | Version | Purpose |
|-------------|---------|---------|
| React | 18.2.0 | UI library |
| Redux | 4.1.0 | State management |
| React Router | 6.14.2 | Client-side routing |
| Axios | 1.6.0 | HTTP client |
| TypeScript | 4.9.5 | Type safety |
| React Modal | 3.16.1 | Modal components |
| JWT Decode | 3.1.2 | JWT token decoding |

## 🚀 Getting Started

### Prerequisites
- Java 11 or higher
- Maven 3.6+
- Node.js 14+ and npm
- MySQL Server 5.7+

### Backend Setup

1. Navigate to the backend directory:
```bash
cd backend/Coupons-Phase2
```

2. Configure MySQL connection in `application.properties`

3. Build the project:
```bash
mvn clean install
```

4. Run the application:
```bash
mvn spring-boot:run
```

The backend API will be available at `http://localhost:8080`

### Frontend Setup

1. Navigate to the frontend directory:
```bash
cd frontend/coupons-react
```

2. Install dependencies:
```bash
npm install
```

3. Start the development server:
```bash
npm start
```

The frontend will be available at `http://localhost:3000`

## 📝 Available Scripts

### Frontend
- `npm start` - Run development server
- `npm build` - Build for production
- `npm test` - Run tests in watch mode
- `npm eject` - Eject from Create React App (one-way operation)

### Backend
- `mvn clean install` - Clean and build project
- `mvn spring-boot:run` - Run the application
- `mvn test` - Run unit and integration tests
- `mvn clean package` - Package for deployment

## 🔐 Security Features

- **JWT Authentication**: Token-based user authentication
- **Password Encryption**: Secure password hashing and storage
- **Role-Based Authorization**: Fine-grained access control
- **Input Validation**: Server-side validation of all inputs
- **CORS Configuration**: Controlled cross-origin resource sharing

## 🗄️ Database

The application uses MySQL with Spring Data JPA for ORM. Key entities include:
- Users (admin and customers)
- Coupons
- Companies
- Purchase history
- Categories

## 📚 API Endpoints

The backend provides RESTful endpoints for:
- User authentication and management
- Coupon CRUD operations
- Company management
- Category management
- Purchase history
- Admin dashboard data

Detailed API documentation is available through Swagger (if configured) or in the controller classes.

## 🧪 Testing

- **Backend**: Unit and integration tests using JUnit
- **Frontend**: React testing library for component testing

Run tests with:
```bash
# Backend
mvn test

# Frontend
npm test
```

## 📦 Project Status

This is **Phase 2** of the Coupon System project, indicating an established and feature-rich application with multiple iterations and improvements.

## 🤝 Development Workflow

1. Make changes in a feature branch
2. Run linters and tests locally
3. Create a pull request
4. Code review and merge to main branch
5. Deploy to production environment

## 📝 Git Configuration

Repository: `oren24/coupons-system`  
Git root: `coupons-system/`

## 🔗 Useful Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [React Documentation](https://react.dev)
- [Redux Documentation](https://redux.js.org)
- [JWT Documentation](https://jwt.io)
- [MySQL Documentation](https://dev.mysql.com/doc)

## 📄 License

This project is part of the Java BootCamp curriculum materials.

---

**Last Updated**: 2026-05-21  
**Project Owner**: Oren  
**Repository**: oren24/coupons-system
