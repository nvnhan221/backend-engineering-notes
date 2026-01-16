# API Reference

Complete API reference documentation.

## Overview

This section contains detailed API documentation for all endpoints, methods, and data structures.

## Endpoints

### Authentication

- `POST /api/auth/login` - User login
- `POST /api/auth/logout` - User logout
- `POST /api/auth/refresh` - Refresh access token

### Users

- `GET /api/users` - List all users
- `GET /api/users/{id}` - Get user by ID
- `POST /api/users` - Create new user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

## Data Models

### User

```json
{
  "id": "string",
  "email": "string",
  "name": "string",
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

## Authentication

All API requests require authentication using Bearer tokens:

```http
Authorization: Bearer <your-token>
```

## Rate Limiting

API requests are rate-limited to 100 requests per minute per IP address.

## Error Responses

All errors follow this format:

```json
{
  "error": {
    "code": "ERROR_CODE",
    "message": "Human-readable error message",
    "details": {}
  }
}
```

## Status Codes

- `200 OK` - Request successful
- `201 Created` - Resource created
- `400 Bad Request` - Invalid request
- `401 Unauthorized` - Authentication required
- `403 Forbidden` - Insufficient permissions
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

---

*API documentation is automatically generated from code annotations.*
