# Vehicle Auction API - Project Structure

## Overview
**Vehicle Auction API** is a high-performance RESTful API for a real-time vehicle auction system built with Spring Boot 3.5.11 and Java 21.

---

## Project Information
- **Group ID**: com.example
- **Artifact ID**: vehicle-auction-api
- **Version**: 0.0.1-SNAPSHOT
- **Name**: vehicle-auction
- **Java Version**: 21
- **Build Tool**: Maven

---

## Directory Structure

```
vehicle-auction-api/
├── .mvn/                              # Maven wrapper configuration
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/vehicle_auction/
│   │   │       ├── VehicleAuctionApplication.java       # Main Spring Boot application
│   │   │       ├── application/                          # Application/Use Case Layer (Clean Architecture)
│   │   │       │   ├── dto/                              # Data Transfer Objects
│   │   │       │   ├── listener/                         # Event listeners
│   │   │       │   ├── mapper/                           # MapStruct mappers for DTO conversion
│   │   │       │   ├── port/                             # Ports (interfaces for hexagonal architecture)
│   │   │       │   └── usecase/                          # Business logic/use cases
│   │   │       ├── domain/                               # Domain Layer (Business Core)
│   │   │       │   ├── model/                            # Domain models
│   │   │       │   │   ├── UserModel.java
│   │   │       │   │   ├── AccountModel.java
│   │   │       │   │   ├── AuctionModel.java
│   │   │       │   │   ├── ProductModel.java
│   │   │       │   │   ├── CategoryModel.java
│   │   │       │   │   ├── BidModel.java
│   │   │       │   │   ├── OrderModel.java
│   │   │       │   │   ├── DepositModel.java
│   │   │       │   │   ├── RoleModel.java
│   │   │       │   │   ├── PermissionModel.java
│   │   │       │   │   ├── ProductImageModel.java
│   │   │       │   │   ├── NotificationModel.java
│   │   │       │   │   ├── WatchlistModel.java
│   │   │       │   │   └── base/                         # Base model classes
│   │   │       │   ├── repository/                       # Repository interfaces
│   │   │       │   ├── event/                            # Domain events
│   │   │       │   ├── enums/                            # Domain enums
│   │   │       │   └── exception/                        # Domain exceptions
│   │   │       ├── infrastructure/                       # Infrastructure Layer
│   │   │       │   ├── configuration/                    # Spring configurations
│   │   │       │   ├── security/                         # JWT security components
│   │   │       │   │   ├── JwtAuthenticationFilter.java
│   │   │       │   │   ├── JwtProperties.java
│   │   │       │   │   ├── JwtService.java
│   │   │       │   │   ├── CustomUserDetails.java
│   │   │       │   │   └── CustomUserDetailsService.java
│   │   │       │   ├── persistence/                      # Database persistence
│   │   │       │   │   ├── entity/                       # JPA entities
│   │   │       │   │   ├── repository/                   # Spring Data JPA repositories
│   │   │       │   │   ├── document/                     # MongoDB documents
│   │   │       │   │   └── mapper/                       # Entity to Model mappers
│   │   │       │   ├── mail/                             # Email service
│   │   │       │   ├── scheduler/                        # Scheduled tasks/background jobs
│   │   │       │   └── seeder/                           # Database seeders for initial data
│   │   │       └── presentation/                         # Presentation/API Layer
│   │   │           ├── controller/                       # REST controllers
│   │   │           ├── advice/                           # Global exception handlers
│   │   │           └── response/                         # API response DTOs
│   │   └── resources/
│   │       ├── application.yaml                          # Main application configuration
│   │       ├── application-dev.yaml                      # Development profile configuration
│   │       ├── logback-spring.xml                        # Logging configuration
│   │       ├── messages.properties                       # Internationalization properties
│   │       └── db/migration/                             # Flyway database migrations
│   │           ├── V1__create_permissions_table.sql
│   │           ├── V2__create_roles_and_mappings.sql
│   │           ├── V3__seed_initial_roles_and_permissions_data.sql
│   │           ├── V4__create_accounts_and_permissions_tables.sql
│   │           ├── V5__create_categories_products_product_image_and_auctions_tables.sql
│   │           ├── V6__create_deposits_orders_bids.sql
│   │           ├── V7__add_verification_token_column_in_account_table.sql
│   │           └── V8__create_watchlist_table.sql
│   └── test/
│       └── java/
│           └── com/example/vehicle_auction/
│               └── VehicleAuctionApplicationTests.java   # Integration tests
├── target/                            # Maven build output
├── logs/
│   └── info.log                       # Application logs
├── .env                               # Environment variables (production)
├── .env.example                       # Example environment variables
├── .gitignore                         # Git ignore rules
├── .gitattributes                     # Git attributes
├── .idea/                             # IntelliJ IDEA configuration
├── .git/                              # Git repository
├── Dockerfile                         # Docker container configuration
├── docker-compose.yml                 # Docker Compose for multi-container setup
├── LICENSE                            # Project license
├── README.md                          # Project README
├── pom.xml                            # Maven configuration
├── mvnw                               # Maven wrapper (Unix)
└── mvnw.cmd                           # Maven wrapper (Windows)
```

---

## Architecture Pattern

This project follows **Clean Architecture** with **Hexagonal Architecture (Ports & Adapters)** principles:

### Layers:
1. **Presentation Layer** (`presentation/`)
   - REST Controllers
   - Global Exception Advice
   - API Response formatting

2. **Application Layer** (`application/`)
   - Use Cases/Business Logic
   - DTOs for data transfer
   - Mappers for transforming data
   - Event Listeners
   - Ports for hexagonal architecture

3. **Domain Layer** (`domain/`)
   - Core business models
   - Repository interfaces
   - Domain events
   - Domain enums and exceptions
   - Pure business logic

4. **Infrastructure Layer** (`infrastructure/`)
   - Spring configurations
   - JPA/Hibernate persistence
   - MongoDB integration
   - Security (JWT)
   - Email service
   - Database migration (Flyway)
   - Scheduled tasks

---

## Key Technologies & Dependencies

### Framework & Core
- **Spring Boot** 3.5.11
- **Java** 21
- **Maven** - Build automation

### Data Access
- **Spring Data JPA** - ORM with Hibernate
- **PostgreSQL** - Relational database
- **Spring Data MongoDB** - NoSQL database
- **Flyway** - Database migrations

### Security
- **Spring Security** - Authentication & Authorization
- **JWT (JJWT)** 0.13.0 - JSON Web Tokens
- **jjwt-api** - JWT API

### Web & API
- **Spring Web MVC** - REST API framework
- **Spring WebSocket** - Real-time communication
- **SpringDoc OpenAPI** 2.8.15 - Swagger/OpenAPI documentation

### Caching & Performance
- **Spring Data Redis** - Redis integration
- **Spring Cache** - Caching abstraction

### Code Generation & Mapping
- **MapStruct** 1.6.3 - Object mapping
- **Lombok** - Reduce boilerplate code

### Validation & Configuration
- **Spring Validation** - Bean validation
- **YAML** - Configuration management

### Logging
- **Logback** - Logging framework (via logback-spring.xml)

---

## Database Schema

### Tables Created (via Flyway Migrations):

1. **Permissions** - System permissions
2. **Roles** - User roles
3. **Role-Permission Mappings** - Role-to-permission relationships
4. **Accounts** - User accounts with verification tokens
5. **Categories** - Product categories
6. **Products** - Vehicle products
7. **Product Images** - Product image references
8. **Auctions** - Auction information
9. **Deposits** - User deposits
10. **Orders** - Purchase orders
11. **Bids** - Auction bids
12. **Watchlist** - User watchlisted items

---

## Core Domain Models

- **UserModel** - User information
- **AccountModel** - Account credentials and details
- **AuctionModel** - Auction details
- **ProductModel** - Vehicle product details
- **CategoryModel** - Product categories
- **BidModel** - Auction bids
- **OrderModel** - Purchase orders
- **DepositModel** - User deposits
- **RoleModel** - User roles
- **PermissionModel** - System permissions
- **ProductImageModel** - Product images
- **NotificationModel** - User notifications
- **WatchlistModel** - User watchlist items

---

## Configuration Files

### Application Configuration
- **application.yaml** - Default configuration
- **application-dev.yaml** - Development profile
- **logback-spring.xml** - Logging configuration
- **messages.properties** - Internationalization

### Container & Deployment
- **Dockerfile** - Docker image definition
- **docker-compose.yml** - Multi-container orchestration
- **.env** - Environment variables
- **.env.example** - Example environment template

---

## Build & Development

### Build Commands
```bash
# Using Maven wrapper
./mvnw clean install
./mvnw spring-boot:run

# Windows
mvnw.cmd clean install
mvnw.cmd spring-boot:run
```

### Docker
```bash
docker-compose up -d
```

---

## Project Features

Based on the structure and models, the application likely supports:

- **User Management** - Account creation, authentication, verification
- **Real-time Auctions** - Auction creation, bidding (WebSocket support)
- **Product Catalog** - Categories, products, images
- **Shopping Cart/Watchlist** - Product bookmarking
- **Order Management** - Purchase orders, deposits
- **Role-Based Access Control** - Permissions and roles
- **Email Notifications** - Verification, order confirmations
- **Caching** - Performance optimization via Redis
- **API Documentation** - Swagger/OpenAPI via SpringDoc
- **Database Migration** - Version control via Flyway

---

## Testing

Integration tests are available in `src/test/java/`
- **VehicleAuctionApplicationTests.java** - Main test class

---

## Logging

- Log files are stored in `logs/` directory
- **logback-spring.xml** defines logging levels and output
- **messages.properties** for localized log messages

---

## Development Setup

1. Clone the repository
2. Configure environment variables (`.env`)
3. Set up PostgreSQL and MongoDB databases
4. Run database migrations via Flyway
5. Start the application: `./mvnw spring-boot:run`
6. Access API documentation at `/swagger-ui.html`

---

## Notes

- The project uses **Java 21** features
- Implements **Clean Architecture** for maintainability
- Uses **JWT** for stateless authentication
- Supports **real-time features** via WebSocket
- Multi-database approach (PostgreSQL + MongoDB)
- Event-driven architecture with domain events

