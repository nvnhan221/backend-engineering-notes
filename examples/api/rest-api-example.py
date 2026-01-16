"""
REST API Example
A simple REST API using Flask
"""

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

    def put(self, user_id):
        """Update a user"""
        user = next((u for u in users if u["id"] == user_id), None)
        if not user:
            return jsonify({"error": "User not found"}), 404

        data = request.get_json()
        if "name" in data:
            user["name"] = data["name"]
        if "email" in data:
            user["email"] = data["email"]
        return jsonify(user)

    def delete(self, user_id):
        """Delete a user"""
        global users
        user = next((u for u in users if u["id"] == user_id), None)
        if not user:
            return jsonify({"error": "User not found"}), 404
        users = [u for u in users if u["id"] != user_id]
        return jsonify({"message": "User deleted"}), 200


# Register routes
user_view = UserAPI.as_view("user_api")
app.add_url_rule("/api/users", view_func=user_view, methods=["GET", "POST"])
app.add_url_rule(
    "/api/users/<int:user_id>", view_func=user_view, methods=["GET", "PUT", "DELETE"]
)


@app.route("/")
def index():
    """Health check endpoint"""
    return jsonify({"status": "ok", "message": "REST API is running"})


if __name__ == "__main__":
    app.run(debug=True, port=5000)
