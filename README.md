# EUCL Prepaid Token System - Backend API

Spring Boot backend for electricity prepaid token generation and management system.

## Table of Contents
- [Technologies](#technologies)
- [Setup](#setup)
- [Configuration](#configuration)
- [API Endpoints](#api-endpoints)
  - [Authentication](#authentication)
  - [User Management](#user-management)
  - [Meter Management](#meter-management)
  - [Token Operations](#token-operations)
  - [Notifications](#notifications)
- [Database Schema](#database-schema)
- [Swagger Documentation](#swagger-documentation)
- [Testing](#testing)
- [Scheduled Tasks](#scheduled-tasks)

## Technologies
- Java 17
- Spring Boot 3.x
- Spring Security
- JWT Authentication
- PostgreSQL
- Spring Data JPA
- Swagger/OpenAPI 3.0
- Spring Mail

## Setup

1. **Prerequisites**:
   - Java 17 JDK
   - PostgreSQL 14+
   - Maven 3.8+

2. **Installation**:
   ```bash
   git clone https://github.com/your-repo/eucl-prepaid-system.git
   cd eucl-prepaid-system
   mvn clean install
   ```

3. **Database Setup**:
   ```sql
   CREATE DATABASE eucl_prepaid;
   CREATE USER eucl_user WITH PASSWORD 'yourpassword';
   GRANT ALL PRIVILEGES ON DATABASE eucl_prepaid TO eucl_user;
   ```

## Configuration

Edit `application.properties`:
```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/eucl_prepaid
spring.datasource.username=eucl_user
spring.datasource.password=yourpassword

# JWT
jwt.secret=your-very-secure-secret-key
jwt.expiration.ms=86400000 # 24 hours

# Email
spring.mail.host=smtp.example.com
spring.mail.port=587
spring.mail.username=your-email@example.com
spring.mail.password=your-email-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# App Settings
app.max-token-days=1825 # 5 years in days
app.token-value-per-day=100 # RWF per day
```

## API Endpoints

### Authentication

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/api/auth/signin` | User login | Public |
| POST | `/api/auth/signup` | User registration | Public |
| POST | `/api/auth/create-admin` | Create admin user (one-time) | Public |

### User Management

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/api/admin/users` | Get all users with meters | ADMIN |
| GET | `/api/admin/users/{userId}` | Get specific user with meters | ADMIN |

### Meter Management

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/api/meters/register` | Register new meter | ADMIN |
| GET | `/api/meters` | List all meters | ADMIN |

### Token Operations

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/api/tokens/purchase` | Purchase electricity token | CUSTOMER |
| GET | `/api/tokens/my-tokens` | Get user's tokens | CUSTOMER |
| GET | `/api/tokens/meter/{meterNumber}` | Get tokens for meter | CUSTOMER,ADMIN |
| GET | `/api/tokens/validate/{token}` | Validate token | CUSTOMER,ADMIN |
| GET | `/api/tokens` | Get all tokens (admin) | ADMIN |

### Notifications

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/api/notifications` | Get all notifications | ADMIN |
| GET | `/api/notifications/my-notifications` | Get user's notifications | CUSTOMER |
| GET | `/api/notifications/meter/{meterNumber}` | Get notifications for meter | CUSTOMER |
| POST | `/api/notifications/test-expiration/{tokenId}` | Trigger test notification | ADMIN |

## Database Schema

![Database Diagram](docs/db-diagram.png)

Main tables:
- `users` - User accounts
- `roles` - User roles (ADMIN, CUSTOMER)
- `meters` - Electricity meters
- `purchased_tokens` - Generated tokens
- `notifications` - Expiration notifications

## Swagger Documentation

Access API documentation at:
```
http://localhost:8080/swagger-ui/index.html
```

Key features:
- Interactive API testing
- Model definitions
- Authentication support (use "Authorize" button with JWT token)

## Testing

1. **Manual Testing**:
   - Use Swagger UI at `/swagger-ui/index.html`
   - Or import Postman collection from `docs/postman-collection.json`

2. **Test Users**:
   - Admin: `admin@eucl.rw` / `admin123`
   - Customer: `customer@example.com` / `password123`

## Scheduled Tasks

- **Token Expiration Check**: Runs hourly to find tokens expiring in 5 hours
- **Notification Service**: Creates DB entries and sends emails

## Deployment

1. Build JAR:
   ```bash
   mvn clean package
   ```

2. Run:
   ```bash
   java -jar target/eucl-prepaid-system-0.0.1-SNAPSHOT.jar
   ```

3. Docker (optional):
   ```dockerfile
   FROM openjdk:17-jdk-slim
   COPY target/*.jar app.jar
   ENTRYPOINT ["java","-jar","/app.jar"]
   ```
```

### Additional Recommendations:

1. Create a `docs/` folder with:
   - `db-diagram.png` - Database schema diagram
   - `postman-collection.json` - Postman export
   - `swagger-screenshot.png` - Example of Swagger UI

2. For the database diagram, you can use:
   - [DBDiagram.io](https://dbdiagram.io)
   - MySQL Workbench
   - pgAdmin 4's diagram tool

3. Include sample requests/responses in the documentation if needed
