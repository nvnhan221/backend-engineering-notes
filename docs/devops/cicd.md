# CI/CD

Continuous Integration and Continuous Deployment practices.

## Overview

CI/CD automates the process of building, testing, and deploying applications.

## What is CI/CD?

### Continuous Integration (CI)

Automatically build and test code changes when committed to version control.

### Continuous Deployment (CD)

Automatically deploy code changes to production after passing tests.

## CI/CD Pipeline Stages

### 1. Source

Code is committed to version control (Git).

### 2. Build

Compile code and create artifacts.

### 3. Test

Run automated tests (unit, integration, e2e).

### 4. Deploy

Deploy to staging/production environments.

### 5. Monitor

Monitor application health and performance.

## Benefits

- ✅ **Faster releases** - Automate manual processes
- ✅ **Higher quality** - Catch bugs early
- ✅ **Reduced risk** - Smaller, frequent deployments
- ✅ **Better collaboration** - Shared responsibility
- ✅ **Faster feedback** - Immediate test results

## CI/CD Tools

### GitHub Actions

```yaml
name: CI/CD Pipeline

on:
  push:
    branches: [main]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: Build
        run: mvn clean package
      - name: Test
        run: mvn test
      - name: Deploy
        run: ./deploy.sh
```

### Jenkins

```groovy
pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
        stage('Deploy') {
            steps {
                sh './deploy.sh'
            }
        }
    }
}
```

### GitLab CI

```yaml
stages:
  - build
  - test
  - deploy

build:
  stage: build
  script:
    - mvn clean package

test:
  stage: test
  script:
    - mvn test

deploy:
  stage: deploy
  script:
    - ./deploy.sh
```

## Pipeline Best Practices

### 1. Fast Feedback

Keep build times short for quick feedback.

```yaml
# Use caching
- name: Cache dependencies
  uses: actions/cache@v3
  with:
    path: ~/.m2
    key: ${{ runner.os }}-m2-${{ hashFiles('**/pom.xml') }}
```

### 2. Parallel Execution

Run tests in parallel to reduce time.

```yaml
jobs:
  test:
    strategy:
      matrix:
        os: [ubuntu-latest, windows-latest, macos-latest]
    runs-on: ${{ matrix.os }}
```

### 3. Environment Promotion

Deploy through environments: dev → staging → production.

```yaml
deploy-dev:
  environment: development
  
deploy-staging:
  environment: staging
  needs: deploy-dev
  
deploy-prod:
  environment: production
  needs: deploy-staging
```

### 4. Rollback Strategy

Have a plan to rollback if deployment fails.

```yaml
deploy:
  steps:
    - name: Deploy
      run: ./deploy.sh
    - name: Health Check
      run: ./health-check.sh
    - name: Rollback on failure
      if: failure()
      run: ./rollback.sh
```

## Testing in CI/CD

### Unit Tests

```yaml
- name: Run unit tests
  run: mvn test
```

### Integration Tests

```yaml
- name: Start services
  run: docker-compose up -d
  
- name: Run integration tests
  run: mvn verify
  
- name: Stop services
  run: docker-compose down
```

### E2E Tests

```yaml
- name: Run E2E tests
  run: |
    npm install
    npm run test:e2e
```

## Deployment Strategies

### Blue-Green Deployment

Deploy new version alongside old, switch traffic when ready.

### Canary Deployment

Gradually roll out to a subset of users.

### Rolling Deployment

Gradually replace old instances with new ones.

## Security in CI/CD

### Secrets Management

```yaml
- name: Deploy
  env:
    API_KEY: ${{ secrets.API_KEY }}
  run: ./deploy.sh
```

### Dependency Scanning

```yaml
- name: Scan dependencies
  run: mvn dependency-check:check
```

### Container Scanning

```yaml
- name: Scan container
  run: trivy image my-app:latest
```

## Monitoring and Alerts

### Deployment Notifications

```yaml
- name: Notify on success
  if: success()
  run: |
    curl -X POST $SLACK_WEBHOOK \
      -d '{"text":"Deployment successful"}'
```

### Health Checks

```yaml
- name: Health check
  run: |
    for i in {1..30}; do
      if curl -f http://app/health; then
        echo "Health check passed"
        exit 0
      fi
      sleep 10
    done
    exit 1
```

## Further Reading

- [GitHub Actions](github-actions.md)
- [Docker](docker.md)
- [Kubernetes & AKS](kubernetes-aks.md)

---

*Last updated: {{ git_revision_date_localized }}*
