# GitHub Actions

Automation and CI/CD with GitHub Actions.

## Overview

GitHub Actions is a CI/CD platform that allows you to automate workflows directly in your GitHub repository.

## Key Concepts

### Workflow

An automated procedure defined in a YAML file in `.github/workflows/`.

### Job

A set of steps that execute on the same runner.

### Step

An individual task that can run commands or use actions.

### Action

A reusable unit of code that performs a specific task.

## Basic Workflow

### Simple Workflow

```yaml
name: CI

on:
  push:
    branches: [main]
  pull_request:
    branches: [main]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: Run tests
        run: npm test
```

## Triggers

### Push and Pull Request

```yaml
on:
  push:
    branches: [main, develop]
  pull_request:
    branches: [main]
```

### Scheduled

```yaml
on:
  schedule:
    - cron: '0 0 * * *'  # Daily at midnight
```

### Manual Trigger

```yaml
on:
  workflow_dispatch:
    inputs:
      environment:
        description: 'Environment to deploy to'
        required: true
        type: choice
        options:
          - staging
          - production
```

### Multiple Events

```yaml
on:
  push:
    branches: [main]
  pull_request:
    branches: [main]
  release:
    types: [published]
```

## Jobs and Steps

### Multiple Jobs

```yaml
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - run: mvn clean package
  
  test:
    runs-on: ubuntu-latest
    needs: build
    steps:
      - uses: actions/checkout@v4
      - run: mvn test
```

### Matrix Strategy

```yaml
jobs:
  test:
    strategy:
      matrix:
        java-version: [8, 11, 17]
        os: [ubuntu-latest, windows-latest]
    runs-on: ${{ matrix.os }}
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v3
        with:
          java-version: ${{ matrix.java-version }}
      - run: mvn test
```

## Common Actions

### Checkout Code

```yaml
- uses: actions/checkout@v4
```

### Setup Java

```yaml
- uses: actions/setup-java@v3
  with:
    java-version: '17'
    distribution: 'temurin'
```

### Setup Node.js

```yaml
- uses: actions/setup-node@v3
  with:
    node-version: '18'
```

### Setup Python

```yaml
- uses: actions/setup-python@v4
  with:
    python-version: '3.11'
```

### Cache Dependencies

```yaml
- uses: actions/cache@v3
  with:
    path: ~/.m2
    key: ${{ runner.os }}-m2-${{ hashFiles('**/pom.xml') }}
    restore-keys: |
      ${{ runner.os }}-m2-
```

## Environment Variables and Secrets

### Environment Variables

```yaml
env:
  NODE_ENV: production
  API_URL: https://api.example.com

jobs:
  build:
    steps:
      - name: Use env var
        run: echo $NODE_ENV
```

### Secrets

```yaml
jobs:
  deploy:
    steps:
      - name: Deploy
        env:
          API_KEY: ${{ secrets.API_KEY }}
        run: ./deploy.sh
```

## Conditions and Expressions

### Conditional Steps

```yaml
steps:
  - name: Deploy to staging
    if: github.ref == 'refs/heads/develop'
    run: ./deploy-staging.sh
  
  - name: Deploy to production
    if: github.ref == 'refs/heads/main'
    run: ./deploy-prod.sh
```

### Expressions

```yaml
steps:
  - name: Run on main branch
    if: github.ref == 'refs/heads/main' && github.event_name == 'push'
    run: echo "Deploying to production"
```

## Artifacts

### Upload Artifact

```yaml
- name: Upload artifact
  uses: actions/upload-artifact@v3
  with:
    name: my-artifact
    path: target/my-app.jar
```

### Download Artifact

```yaml
- name: Download artifact
  uses: actions/download-artifact@v3
  with:
    name: my-artifact
```

## Deployment

### Deploy to GitHub Pages

```yaml
- name: Deploy to GitHub Pages
  uses: peaceiris/actions-gh-pages@v3
  with:
    github_token: ${{ secrets.GITHUB_TOKEN }}
    publish_dir: ./dist
```

### Deploy to Azure

```yaml
- name: Deploy to Azure
  uses: azure/webapps-deploy@v2
  with:
    app-name: my-app
    publish-profile: ${{ secrets.AZURE_WEBAPP_PUBLISH_PROFILE }}
    package: .
```

## Notifications

### Slack Notification

```yaml
- name: Notify Slack
  uses: 8398a7/action-slack@v3
  with:
    status: ${{ job.status }}
    text: 'Deployment completed'
  env:
    SLACK_WEBHOOK_URL: ${{ secrets.SLACK_WEBHOOK }}
```

## Best Practices

1. **Use specific action versions** - Pin to major version
2. **Cache dependencies** - Speed up builds
3. **Use matrix builds** - Test multiple configurations
4. **Fail fast** - Stop on first error
5. **Use secrets** - Never commit sensitive data
6. **Organize workflows** - Separate concerns
7. **Add status badges** - Show build status in README

## Example: Complete CI/CD Workflow

```yaml
name: CI/CD Pipeline

on:
  push:
    branches: [main]
  pull_request:
    branches: [main]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      
      - uses: actions/setup-java@v3
        with:
          java-version: '17'
      
      - uses: actions/cache@v3
        with:
          path: ~/.m2
          key: ${{ runner.os }}-m2-${{ hashFiles('**/pom.xml') }}
      
      - name: Build
        run: mvn clean package -DskipTests
      
      - name: Upload artifact
        uses: actions/upload-artifact@v3
        with:
          name: app.jar
          path: target/app.jar
  
  test:
    runs-on: ubuntu-latest
    needs: build
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v3
        with:
          java-version: '17'
      - run: mvn test
  
  deploy:
    if: github.ref == 'refs/heads/main'
    needs: [build, test]
    runs-on: ubuntu-latest
    steps:
      - uses: actions/download-artifact@v3
        with:
          name: app.jar
      - name: Deploy
        run: ./deploy.sh
```

## Further Reading

- [CI/CD](cicd.md)
- [Docker](docker.md)
- [Kubernetes & AKS](kubernetes-aks.md)

---

*Last updated: {{ git_revision_date_localized }}*
