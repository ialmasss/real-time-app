# Real-Time Chat Application

🔗 **Live demo:** https://real-time-app-ltho.onrender.com

A full-featured real-time chat application built with Spring Boot, featuring WebSocket-based messaging, horizontal scaling via Redis Pub/Sub, and Docker containerization.

## Features

- User registration and authentication (JWT)
- One-on-one and group chats
- Real-time message delivery via WebSocket (STOMP)
- Online/offline presence tracking
- "Typing..." indicator
- Read receipts
- Horizontal scaling via Redis Pub/Sub — multiple backend instances share message delivery
- Containerized with Docker and Docker Compose

## Tech Stack

**Backend:** Java 21, Spring Boot 4, Spring Security, Spring WebSocket (STOMP), Spring Data JPA

**Database:** PostgreSQL 17

**Real-time & scaling:** Redis 8 (Pub/Sub)

**Infrastructure:** Docker, Docker Compose

**Load testing:** k6

## Architecture

```mermaid
graph TB
    Client[Client]
    Backend1[Backend #1<br/>Spring Boot]
    Backend2[Backend #2<br/>Spring Boot]
    Redis[(Redis<br/>Pub/Sub)]
    Postgres[(PostgreSQL)]

    Client -->|WebSocket / REST| Backend1
    Client -->|WebSocket / REST| Backend2
    Backend1 <--> Redis
    Backend2 <--> Redis
    Backend1 --> Postgres
    Backend2 --> Postgres
```

## Demo: cross-instance delivery

A message sent through the backend on port 8081 is delivered in real time to a client connected to the backend on port 8082, via Redis Pub/Sub.

![Two instances demo](docs/testingrealtime.gif)

The core idea: multiple backend instances run independently but stay in sync via Redis Pub/Sub. A user connected to Backend #1 receives messages sent by a user connected to Backend #2 instantly — without this synchronization, the message would stay confined to a single process.
## Running the Project

### Option 1 — Local (for development)

Requires: Java 21, Maven, PostgreSQL, Redis.

```bash
# create a PostgreSQL database named realtime_app
# configure application.properties (see application.properties.example)

mvn clean package
java -jar target/*.jar
```

### Option 2 — Docker Compose (multiple instances)

Requires: Docker Desktop.

```bash
docker-compose up --build
```

This spins up:
- PostgreSQL on port 5432
- Redis on port 6379
- Backend #1 on port 8081
- Backend #2 on port 8082

Both backend instances share the same database and Redis instance, demonstrating horizontal scaling.

## Load Testing

Load testing was performed using [k6](https://k6.io/) — the script simulates multiple concurrent users logging in.

Test script: [`load-tests/login-test.js`](./load-tests/login-test.js)

### Results

| Virtual Users | Requests/sec | Avg Response Time | 95th Percentile | Errors |
|---|---|---|---|---|
| 10  | 8.4  | 177 ms | 213 ms  | 0%    |
| 50  | 40.8 | 202 ms | 305 ms  | 0%    |
| 100 | 85.0 | 129–152 ms | 165–413 ms | 0%    |
| 200 | 97.6 | 979 ms | 1180 ms | 2.78% |

### Findings

The system handles up to **100 concurrent users** reliably, with zero errors and response times in the 150–200 ms range. At a sharp spike to **200 concurrent users**, performance degrades noticeably — average response time grows nearly 5x, and a 2.78% connection failure rate appears.

The likely cause is connection pool exhaustion (HikariCP defaults to 10 connections per instance). The next step for further scaling would be increasing the connection pool size and re-testing, or adding a third backend instance to distribute the load.

Full test results are available in the [`load-tests/`](./load-tests/) directory.

## Security

- Passwords are hashed with BCrypt before being stored in the database
- Authentication via JWT tokens (for both REST API and WebSocket connections)
- REST endpoints are protected via Spring Security
- WebSocket connections are authenticated through a custom interceptor at the STOMP CONNECT stage

## Limitations & Design Notes

This project is a portfolio/learning project. These are the known trade-offs:

- **Only chat messages go through Redis Pub/Sub.** New messages are published to Redis and delivered to clients on every backend instance. Typing indicators, online/offline presence and read receipts are broadcast only within the instance that receives them, so with several instances they are visible only to clients connected to the same instance.
- **Simple in-memory STOMP broker.** Subscriptions live in each instance's memory. For a production setup a full broker relay (RabbitMQ/ActiveMQ) or a sticky-session load balancer would be used.
- **Authorization is membership-based.** Only chat participants can read history, send messages and subscribe to a chat's topics (REST and WebSocket). There are no roles (admin/moderator) inside group chats.
- **JWT without refresh tokens.** Tokens expire after 24 hours; the user logs in again.
- **No rate limiting.** Login and messaging endpoints are not throttled.
- **Load test covers login only.** The k6 scenario measures `POST /api/auth/login` (CPU-heavy because of BCrypt). Message throughput over WebSocket has not been load-tested. The degradation at 200 VUs is most likely the default HikariCP pool size (10 connections per instance); tuning it is a next step.
- **Free-tier hosting.** The live demo runs on Render's free plan, so the first request after a period of inactivity can take up to a minute while the service wakes up.

## Roadmap

- [x] Real-time messaging (STOMP over WebSocket)
- [x] JWT authentication (REST + WebSocket)
- [x] Multi-instance delivery via Redis Pub/Sub
- [x] Docker / Docker Compose setup
- [x] Load testing with k6
- [x] Live demo deployment
- [x] Chat membership authorization
- [ ] Sync typing / presence / read receipts across instances via Redis
- [ ] Tune the connection pool and re-run load tests
- [ ] Rate limiting
- [ ] Flutter client