# REST API Example

A complete example of building a REST API with CRUD operations.

## Overview

This example demonstrates how to build a RESTful API using Flask with proper error handling, validation, and response formatting.

## Code Example

The complete example code is available in the `examples/api/` directory.

```python
from flask import Flask, jsonify, request
from flask.views import MethodView

app = Flask(__name__)

# In-memory storage (use a database in production)
users = [
    {"id": 1, "name": "John Doe", "email": "john@example.com"},
    {"id": 2, "name": "Jane Smith", "email": "jane@example.com"},
]

class UserAPI(MethodView):
    """User resource endpoints"""
    
    def get(self, user_id=None):
        """Get user(s)"""
        if user_id:
            user = next((u for u in users if u["id"] == user_id), None)
            if not user:
                return jsonify({"error": "User not found"}), 404
            return jsonify(user)
        return jsonify(users)
    
    def post(self):
        """Create a new user"""
        data = request.get_json()
        if not data or "name" not in data or "email" not in data:
            return jsonify({"error": "Name and email are required"}), 400
        
        new_id = max([u["id"] for u in users], default=0) + 1
        user = {
            "id": new_id,
            "name": data["name"],
            "email": data["email"],
        }
        users.append(user)
        return jsonify(user), 201
```

## Running the Example

!!! tip "Quick Start"
    Navigate to `examples/api/` and follow the README instructions.

```bash
cd examples/api
pip install flask
python rest-api-example.py
```

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/users` | List all users |
| GET | `/api/users/{id}` | Get user by ID |
| POST | `/api/users` | Create new user |
| PUT | `/api/users/{id}` | Update user |
| DELETE | `/api/users/{id}` | Delete user |

## Testing with cURL

=== "List Users"
    ```bash
    curl http://localhost:5000/api/users
    ```

=== "Create User"
    ```bash
    curl -X POST http://localhost:5000/api/users \
      -H "Content-Type: application/json" \
      -d '{"name": "Alice", "email": "alice@example.com"}'
    ```

=== "Get User"
    ```bash
    curl http://localhost:5000/api/users/1
    ```

## Best Practices

!!! note "Validation"
    Always validate input data before processing.

!!! warning "Security"
    In production, use a proper database and implement authentication.

!!! tip "Error Handling"
    Return appropriate HTTP status codes and error messages.

## Next Steps

- Add authentication and authorization
- Implement database persistence
- Add request validation with schemas
- Write unit and integration tests
- Add API documentation with Swagger/OpenAPI
