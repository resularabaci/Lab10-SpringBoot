## SECURITY OF WEB APPLICATIONS PROJECT ##
This is a simple Spring Boot application for security of web applications lesson.

# How to Run
1. Run the application
2. Copy the Password from bottom below on the Terminal
3. Access: `http://localhost:8080/hello`
4. Use Credentials for Username : "user" and Password which is copied from Terminal

# Lab 11: JWT Authentication & Roles
## Step 1: Register a Secure User
Invoke-RestMethod -Uri "http://localhost:8080/api/users/register" `
-Method Post `
-ContentType "application/json" `
-Body '{"username": "student", "email": "test@test.com", "password": "password123"}'


## Step 2: Login & Save Token
$resp = Invoke-RestMethod -Uri "http://localhost:8080/auth/login" `
-Method Post `
-ContentType "application/json" `
-Body '{"email": "test@test.com", "password": "password123"}'

$token = $resp.token
Write-Host "Token saved to `$token variable"

## Step 3: Access Protected Endpoints Tests the @PreAuthorize security rules using the Bearer token.
1. User Access (ROLE_USER):
Invoke-RestMethod -Uri "http://localhost:8080/api/users/info" `
-Method Get `
-Headers @{ Authorization = "Bearer $token" }

2. Admin Access (ROLE_ADMIN):
Invoke-RestMethod -Uri "http://localhost:8080/api/users/admin/data" `
-Method Get `
-Headers @{ Authorization = "Bearer $token" }
