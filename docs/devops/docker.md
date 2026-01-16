# Docker

Containerization with Docker for application deployment.

## Overview

Docker is a platform for developing, shipping, and running applications in containers.

## Key Concepts

### Container

A lightweight, standalone, executable package that includes everything needed to run an application.

### Image

A read-only template for creating containers. Images are built from Dockerfiles.

### Dockerfile

A text file with instructions for building a Docker image.

## Installation

### macOS

```bash
# Using Homebrew
brew install --cask docker

# Or download from docker.com
```

### Linux

```bash
# Ubuntu/Debian
curl -fsSL https://get.docker.com -o get-docker.sh
sh get-docker.sh
```

### Windows

Download Docker Desktop from docker.com

## Basic Commands

### Images

```bash
# Pull an image
docker pull nginx

# List images
docker images

# Remove an image
docker rmi nginx

# Build an image
docker build -t my-app .
```

### Containers

```bash
# Run a container
docker run -d -p 8080:80 nginx

# List running containers
docker ps

# List all containers
docker ps -a

# Stop a container
docker stop <container-id>

# Remove a container
docker rm <container-id>

# View logs
docker logs <container-id>

# Execute command in container
docker exec -it <container-id> /bin/bash
```

## Dockerfile

### Basic Dockerfile

```dockerfile
# Use official base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy application files
COPY target/my-app.jar app.jar

# Expose port
EXPOSE 8080

# Run application
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Multi-stage Build

```dockerfile
# Build stage
FROM maven:3.8-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/my-app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

## Docker Compose

### docker-compose.yml

```yaml
version: '3.8'

services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - DATABASE_URL=postgresql://db:5432/mydb
    depends_on:
      - db
  
  db:
    image: postgres:15
    environment:
      POSTGRES_DB: mydb
      POSTGRES_USER: user
      POSTGRES_PASSWORD: password
    volumes:
      - postgres_data:/var/lib/postgresql/data

volumes:
  postgres_data:
```

### Compose Commands

```bash
# Start services
docker-compose up -d

# Stop services
docker-compose down

# View logs
docker-compose logs -f

# Rebuild and restart
docker-compose up -d --build
```

## Best Practices

### 1. Use .dockerignore

```dockerignore
node_modules
.git
*.md
.env
target/
```

### 2. Minimize Layers

```dockerfile
# Bad
RUN apt-get update
RUN apt-get install -y package1
RUN apt-get install -y package2

# Good
RUN apt-get update && \
    apt-get install -y package1 package2 && \
    rm -rf /var/lib/apt/lists/*
```

### 3. Use Specific Tags

```dockerfile
# Bad
FROM node:latest

# Good
FROM node:18-alpine
```

### 4. Don't Run as Root

```dockerfile
FROM openjdk:17-jdk-slim
RUN groupadd -r appuser && useradd -r -g appuser appuser
USER appuser
```

### 5. Use Multi-stage Builds

Reduces final image size by excluding build tools.

## Common Patterns

### Health Checks

```dockerfile
HEALTHCHECK --interval=30s --timeout=3s \
  CMD curl -f http://localhost:8080/health || exit 1
```

### Environment Variables

```dockerfile
ENV APP_ENV=production
ENV APP_PORT=8080
```

### Volumes

```bash
# Named volume
docker volume create mydata
docker run -v mydata:/data my-app

# Bind mount
docker run -v /host/path:/container/path my-app
```

## Networking

### Create Network

```bash
docker network create mynetwork
```

### Connect Containers

```bash
docker run --network mynetwork --name app my-app
docker run --network mynetwork --name db postgres
```

## Further Reading

- [Kubernetes & AKS](kubernetes-aks.md)
- [CI/CD](cicd.md)
- [GitHub Actions](github-actions.md)

---

*Last updated: {{ git_revision_date_localized }}*
