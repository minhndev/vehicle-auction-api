<h1 align="center">Vehicle Auction API</h1>
<p align="center">High-performance RESTful API for a real-time vehicle auction platform</p>

---

## Overview

Vehicle Auction API is a backend service that powers a real-time vehicle bidding platform. It handles the complete auction lifecycle — from product listing and deposit payment to real-time bidding and post-auction order management — built with a clean **Hexagonal Architecture** to keep the domain logic independent from external systems.

---

## Features

- **Auction Lifecycle Management** — Auctions automatically transition from `UPCOMING` to `ACTIVE` to `CLOSED` via Spring Scheduler & RabbitMQ Delayed Messaging
- **Real-time Bidding** — Live bid updates broadcast to all participants via WebSocket (STOMP)
- **Deposit System** — Users must pay a refundable deposit via VNPay before placing bids
- **Asynchronous Processing** — High-performance event-driven architecture using **RabbitMQ** for reliable auction status transitions, email notifications, and automated deposit refunds
- **Payment & Order Management** — Full VNPay integration with IPN callback handling and automatic refunds for losing bidders
- **Push Notifications** — Real-time notifications via Redis Pub/Sub, stored persistently in MongoDB
- **Authentication & Authorization** — JWT-based auth (Access + Refresh Tokens), Google OAuth2, email verification, and RBAC
- **Product Management** — Vehicle listings with multi-image upload to MinIO/S3, categorization, and status tracking
- **Watchlist** — Users can follow auctions they are interested in
- **API Documentation** — Full OpenAPI 3 / Swagger UI documentation with context-aware error handling

---

## Tech Stack

| Language & Framework | Java 21, Spring Boot 3.5 |
| Security | Spring Security, JWT (JJWT 0.13), Google OAuth2 |
| Primary Database | PostgreSQL 16 (JPA / Hibernate, Flyway migrations) |
| Document Store | MongoDB 6 (notifications history) |
| Cache | Redis 7 |
| Messaging (Broker) | **RabbitMQ** (Event-driven tasks) |
| Real-time | WebSocket / STOMP |
| File Storage | AWS S3 / **MinIO** |
| Payment Gateway | VNPay |
| API Docs | SpringDoc OpenAPI (Swagger UI) |
| DevOps | Docker, Docker Compose |
| Utilities | Lombok, MapStruct, Spring Mail |

---

## Architecture

This project follows **Hexagonal Architecture (Ports & Adapters)**:

```
src/main/java/com/example/vehicle_auction/
├── domain/              # Core business logic: entities, enums, domain events, exceptions
├── application/         # Use cases, DTOs, service interfaces (ports)
└── infrastructure/      # Adapters: JPA repositories, MongoDB, Redis, VNPay, AWS S3, Mail, WebSocket
```

---

## Getting Started

### Prerequisites

- Java 21+
- Docker & Docker Compose

### Environment Variables

Copy `.env.example` to `.env` and fill in your values:

```bash
cp .env.example .env
```

Key variables:

| Variable | Description |
|---|---|
| `SERVER_PORT` | Port the application runs on (default: 8080) |
| `POSTGRES_*` | PostgreSQL connection details |
| `MONGO_URI` | MongoDB connection URI |
| `REDIS_HOST / REDIS_PORT` | Redis connection details |
| `RABBITMQ_*` | RabbitMQ connection host, port, and credentials |
| `JWT_ACCESS_SECRET / JWT_REFRESH_SECRET` | JWT signing secrets |
| `VNPAY_*` | VNPay payment gateway credentials |
| `S3_* / MINIO_*` | S3 compatible storage (MinIO) configuration |
| `GOOGLE_CLIENT_ID / GOOGLE_CLIENT_SECRET` | Google OAuth2 credentials |
| `MAIL_*` | SMTP email configuration |

### Run with Docker Compose

```bash
docker-compose up -d
```

This starts PostgreSQL, MongoDB, Redis, MinIO, and the application together.

### Run Locally

```bash
./mvnw spring-boot:run
```

---

## API Documentation

Once the application is running, access the Swagger UI at:

```
http://localhost:{SERVER_PORT}/swagger-ui.html
```

---

## Authentication Flow

```
Register → Verify Email → Login (JWT) → Access protected endpoints
                                ↑
                         Google OAuth2 Login
```

- Access token + refresh token are issued on login
- Refresh token rotation is supported via `/auth/refresh`
- Password reset is handled via email link

---

## Payment Flow

```
User → Request Deposit → VNPay redirect → IPN Callback → Deposit activated → User can bid
Winner → Checkout → VNPay payment → Order created
Losers → Deposit automatically refunded
```

---

## WebSocket Events

Connect to: `ws://localhost:{SERVER_PORT}/ws`

| Topic | Description |
|---|---|
| `/topic/auction/{auctionId}/bid` | Real-time bid updates for an auction |
| `/topic/auction/{auctionId}/status` | Auction status change notifications |
| `/user/queue/notifications` | Personal notifications for the logged-in user |

---

## Suggestions for Leveling Up (Fresher Focus)

If you're looking to enhance this project further as a fresher, here are some high-impact ideas:

### 1. Robust Testing & Quality

- **Unit & Integration Tests**: Implement `JUnit 5` and `Mockito` for domain logic. Use `Testcontainers` to perform real integration tests with PostgreSQL and Redis.
- **Code Coverage**: Integrate **JaCoCo** to track test coverage (aim for >80% on domain/application layers).
- **Static Analysis**: Add **Checkstyle** or **SonarLint** to ensure clean, consistent code patterns.

### 2. DevOps & Infrastructure

- **CI/CD Pipeline**: Create a `.github/workflows` to automatically build and test every pull request.
- **Observability**: Integrate **Spring Boot Actuator** + **Prometheus/Grafana** for real-time monitoring of application health and metrics.
- **Structured Logging**: Use **MDC** (Mapped Diagnostic Context) to attach a `Trace ID` to every request, making it easier to track flows across logs.

### 3. API & Security Excellence

- **Global Error Handling**: Standardize all API error responses following the **RFC 7807 (Problem Details for HTTP APIs)** format.
- **Rate Limiting**: Use **Redis** to implement rate limiting for sensitive endpoints (e.g., login, bidding).
- **API Versioning**: Move from `/api/v1` to a more scalable versioning strategy in the headers or paths.

### 4. Advanced Domain Features

- **Auditing**: Implement **Spring Data Envers** or custom listeners to track changes to products and auction bids (who updated what and when).
- **Search Optimization**: Integrate **Elasticsearch** (or simple PostgreSQL Full-Text Search) for high-speed vehicle filtering.

---

## License

This project is licensed under the terms of the [LICENSE](LICENSE) file.