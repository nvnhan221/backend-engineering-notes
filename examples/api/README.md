# API Examples

Runnable examples for building REST APIs.

## REST API Example

A simple REST API using Flask demonstrating CRUD operations.

### Prerequisites

```bash
pip install flask
```

### Running the Example

```bash
python rest-api-example.py
```

The API will be available at `http://localhost:5000`

### Endpoints

- `GET /` - Health check
- `GET /api/users` - List all users
- `GET /api/users/{id}` - Get user by ID
- `POST /api/users` - Create new user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

### Example Requests

```bash
# Get all users
curl http://localhost:5000/api/users

# Create a user
curl -X POST http://localhost:5000/api/users \
  -H "Content-Type: application/json" \
  -d '{"name": "Alice", "email": "alice@example.com"}'

# Get user by ID
curl http://localhost:5000/api/users/1

# Update user
curl -X PUT http://localhost:5000/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{"name": "Alice Updated"}'

# Delete user
curl -X DELETE http://localhost:5000/api/users/1
```
