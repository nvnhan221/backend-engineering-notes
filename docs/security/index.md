# Security

Security best practices for backend systems.

## Overview

This section covers security patterns, authentication, authorization, and secure coding practices.

## Topics

- Authentication and authorization
- API security
- Data encryption
- Secrets management
- Security headers
- OWASP Top 10

## Authentication

### JWT (JSON Web Tokens)

```python
import jwt
from datetime import datetime, timedelta

def create_token(user_id):
    payload = {
        'user_id': user_id,
        'exp': datetime.utcnow() + timedelta(hours=1)
    }
    return jwt.encode(payload, SECRET_KEY, algorithm='HS256')
```

### OAuth2

Standard protocol for authorization.

## Authorization

### Role-Based Access Control (RBAC)

Control access based on user roles.

### Attribute-Based Access Control (ABAC)

Control access based on attributes.

## API Security

### Rate Limiting

```python
from flask_limiter import Limiter

limiter = Limiter(
    app,
    key_func=get_remote_address,
    default_limits=["100 per hour"]
)
```

### Input Validation

Always validate and sanitize input.

### HTTPS

Always use HTTPS in production.

## Secrets Management

- Never commit secrets to version control
- Use environment variables or secret managers
- Rotate secrets regularly

## Security Headers

```python
@app.after_request
def set_security_headers(response):
    response.headers['X-Content-Type-Options'] = 'nosniff'
    response.headers['X-Frame-Options'] = 'DENY'
    response.headers['X-XSS-Protection'] = '1; mode=block'
    response.headers['Strict-Transport-Security'] = 'max-age=31536000'
    return response
```

## Best Practices

- ✅ Use HTTPS everywhere
- ✅ Validate all input
- ✅ Use parameterized queries
- ✅ Implement rate limiting
- ✅ Keep dependencies updated
- ✅ Regular security audits

---

*Add more security topics as needed*
