# Auth Module
Handles:
- user registration
- login and password hashing
- token generation (JWT)

Endpoints:
- POST /auth/register
- POST /auth/login

Technically:
Frontend sends login request with email/password

Backend verifies credentials and responds with a JWT

Frontend stores the JWT (in memory or localStorage)

On every subsequent request, the frontend includes the JWT in the Authorization header:

makefile
Copy
Edit
Authorization: Bearer eyJhbGciOiJIUzI1NiIs...
The backend verifies the token using the signing key and:

Authenticates the user

Authorizes access to protected endpoints