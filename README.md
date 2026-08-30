# Nagorik Seba

Civic complaint system for Bangladesh City Corporations and Pourashavas. This repository is **Phase 1**: JWT auth, domain model, and a React SPA shell. Complaint workflow, map, SLA, and notifications are stubbed for later phases.

## Prerequisites

- Java 17+
- Maven Wrapper (`./mvnw`)
- Docker (for PostgreSQL)
- Node.js 18+

## Run locally

1. Start the database:

```bash
docker compose up -d
```

2. Start the API (http://localhost:8080):

```bash
./mvnw spring-boot:run
```

3. Start the React app (http://localhost:5173):

```bash
cd frontend
npm install
npm run dev
```

The Vite dev server proxies `/api` to the Spring Boot backend.

## Seeded accounts

| Role | Email | Password |
| --- | --- | --- |
| Admin | admin@nagorikseba.com | admin123 |
| Citizen | citizen@nagorikseba.com | citizen123 |
| Ward councilor | councilor1@nagorikseba.com | councilor123 |
| Department officer | officer1@nagorikseba.com | officer123 |

Register a new citizen at `/register`, or log in with email **or** phone.

## Working Phase 1 APIs

- `POST /api/auth/register`
- `POST /api/auth/login`
- `POST /api/auth/refresh`
- `GET /api/public/wards`

Other REST endpoints exist and return empty lists or `501 Not Implemented`.
