# 🔐 Supabase Authentication Plan (Angular + Spring Boot + Supabase)

This document outlines how to implement authentication in a fullstack application using Angular, Spring Boot, and Supabase (PostgreSQL-based backend).

---

## 🧭 General Flow Overview

1. The user signs up or logs in via Angular using Supabase Auth.
2. Supabase returns a session containing an access token (JWT) and a refresh token.
3. Angular stores the session and attaches the access token to every backend API call.
4. The Spring Boot backend acts as a resource server, validating the JWT using Supabase’s public keys (JWKS).
5. The backend uses claims from the JWT (e.g., user ID, email) to identify and authorize the user.
6. Supabase handles token refreshing on the client side; the backend only validates existing tokens.

---

## ⚙️ Step 1: Supabase Setup

- Create a Supabase project in the dashboard.
- Note down:
    - Project URL
    - Anon key
- JWT issuer: `https://<PROJECT_REF>.supabase.co/auth/v1`
- JWKS URI: `https://<PROJECT_REF>.supabase.co/auth/v1/keys`
- Configure authentication providers and password login if needed.

---

## 🧩 Step 2: Angular Responsibilities (Frontend)

- Use Supabase JS SDK for authentication.
- Initialize Supabase client with the project URL and anon key.
- Implement an AuthService to:
    - Handle registration and login via Supabase.
    - Store the current session (access and refresh tokens).
    - React to authentication state changes.
    - Expose user information and login status to other parts of the app.
- Add an HTTP interceptor to attach the `Authorization: Bearer <access_token>` header to all outgoing requests to the Spring Boot API.
- Add route guards to restrict access to protected routes based on login state.
- The Supabase SDK automatically handles token refresh on the frontend.
- The frontend environment variables should include the Supabase URL and anon key.

---

## 🛡️ Step 3: Spring Boot Responsibilities (Backend)

- The backend is a **resource server**, not an authentication server.
- It does **not** issue tokens or refresh them.
- It validates incoming JWTs from Supabase using the issuer and JWKS endpoints.
- Once validated, it trusts the token and authorizes access to protected resources.
- Use the claims in the JWT:
    - `sub` → Supabase user ID (primary user identifier)
    - `email` → user’s email
    - other metadata if needed
- The backend can optionally maintain its own user profile table keyed by the Supabase user ID to store application-specific data.
- Security rules:
    - Public endpoints are open (e.g., `/public/**`).
    - All other endpoints require authentication.
- The backend connects to the Supabase Postgres database directly via standard JDBC credentials.
- Authorization decisions are handled by Spring Security after successful token validation.

---

## 🗂️ Step 4: Database and User Profile Handling

- The main database is the Supabase-managed Postgres instance.
- Your Spring Boot backend connects directly to it.
- Optional:
    - Create a `user_profile` table with a foreign key or reference to the Supabase `auth.users` table.
    - Automatically create or update the user profile the first time the user accesses the backend API.
- Alternatively, use Supabase functions or webhooks to notify the backend on user creation.

---

## 🔁 Step 5: Session Lifecycle Management

- **Login / Register:** Angular communicates directly with Supabase Auth.
- **Token Refresh:** Managed automatically by the Supabase JS SDK.
- **Backend Authentication:** Spring Boot validates every request using the current JWT.
- **Logout:** Triggered on the frontend via the Supabase SDK; tokens are cleared locally.
- **Session Persistence:** Supabase stores session info in local storage by default.

---

## 📬 Step 6: Communication Flow Summary

1. User signs up or logs in via Angular.
2. Supabase Auth returns an access token and refresh token.
3. Angular stores the session and adds the access token to API requests.
4. Spring Boot validates the token using Supabase JWKS and authorizes access.
5. Backend processes the request and returns data.
6. Supabase automatically refreshes expired tokens on the client side.

---

## ✅ Implementation Checklist

### Frontend
- Initialize Supabase client.
- Implement AuthService to manage sign-up, sign-in, and session state.
- Add HTTP interceptor to include JWT in requests.
- Add route guards and user store for managing auth state.
- Create login, register, and logout components.
- Store Supabase URL and anon key in environment variables.

### Backend
- Configure Spring Boot as an OAuth2 resource server.
- Validate JWT using Supabase issuer and JWKS endpoints.
- Define authorization rules for public and protected routes.
- Implement endpoints that can extract user info from JWT claims.
- Optionally create a user profile table in the database linked to Supabase user ID.

### Environment
- Angular: `SUPABASE_URL`, `SUPABASE_ANON_KEY`
- Spring Boot: `issuer-uri` and `jwk-set-uri` pointing to Supabase endpoints.
- Database credentials for connecting to Supabase Postgres.

### Testing
- Register or log in a user via Angular.
- Call a secured backend endpoint with the access token.
- Verify that Spring Boot successfully validates the token and returns the expected data.

---

## 🧠 Summary Table

| Responsibility | Layer | Description |
|----------------|--------|-------------|
| Authentication provider | Supabase | Handles sign-up, sign-in, and token management |
| Frontend | Angular | Communicates with Supabase Auth and stores the JWT |
| Backend | Spring Boot | Validates JWTs, enforces access control, and interacts with the database |
| Database | Supabase Postgres | Stores all application data |

---

## 💡 Key Takeaway

Authentication and user management are handled by **Supabase Auth** on the frontend, while **Spring Boot** serves as a **JWT-validating resource server** for your protected APIs. The backend never issues or refreshes tokens — it simply trusts and validates those generated by Supabase.
