<h1 align="center">Vehicle Auction API</h1>
<p align="center">High-performance RESTful API for a real-time vehicle auction platform</p>
<p align="center">
  <img src="https://img.shields.io/badge/Java-21-ED8B00?style=flat&logo=openjdk&logoColor=white"/>
  <img src="https://img.shields.io/badge/Spring_Boot-3.5-6DB33F?style=flat&logo=spring-boot&logoColor=white"/>
  <img src="https://img.shields.io/badge/PostgreSQL-16-316192?style=flat&logo=postgresql&logoColor=white"/>
  <img src="https://img.shields.io/badge/Redis-7-DC382D?style=flat&logo=redis&logoColor=white"/>
  <img src="https://img.shields.io/badge/Docker-Compose-2496ED?style=flat&logo=docker&logoColor=white"/>
</p>

---

## Overview

Vehicle Auction API is a backend service that powers a real-time vehicle bidding platform. It handles the complete auction lifecycle — from product listing and deposit payment to real-time bidding and post-auction order management — built with a clean **Hexagonal Architecture** to keep the domain logic independent from external systems.

---

## Features

- **Auction Lifecycle Management** — Auctions automatically transition from `UPCOMING` to `ACTIVE` to `CLOSED` via Spring Scheduler
- **Real-time Bidding** — Live bid updates broadcast to all participants via WebSocket (STOMP)
- **Deposit System** — Users must pay a refundable deposit via VNPay before placing bids
- **Payment & Order Management** — Full VNPay integration with IPN callback handling and automatic refunds for losing bidders
- **Push Notifications** — Real-time notifications via Redis Pub/Sub, stored persistently in MongoDB
- **Authentication & Authorization** — JWT-based auth, Google OAuth2 login, email verification, password reset, and RBAC (Role & Permission management)
- **Product Management** — Vehicle listings with multi-image upload to AWS S3, categorization, and status tracking
- **Watchlist** — Users can follow auctions they are interested in
- **API Documentation** — Full OpenAPI 3 / Swagger UI documentation

---

## Tech Stack

| Category | Technology |
|---|---|
| Language & Framework | Java 21, Spring Boot 3.5 |
| Security | Spring Security, JWT (JJWT 0.13), Google OAuth2 |
| Primary Database | PostgreSQL 16 (JPA / Hibernate, Flyway migrations) |
| Document Store | MongoDB 6 (notifications history) |
| Cache & Messaging | Redis 7 (caching + Pub/Sub) |
| Real-time | WebSocket / STOMP |
| File Storage | AWS S3 (or MinIO for local dev) |
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
| `SERVER_PORT` | Port the application runs on |
| `POSTGRES_*` | PostgreSQL connection details |
| `MONGO_URI` | MongoDB connection URI |
| `REDIS_HOST / REDIS_PORT` | Redis connection details |
| `JWT_ACCESS_SECRET / JWT_REFRESH_SECRET` | JWT signing secrets |
| `VNPAY_*` | VNPay payment gateway credentials |
| `AWS_S3_*` | AWS S3 (or MinIO) configuration |
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

## License

This project is licensed under the terms of the [LICENSE](LICENSE) file.