# Kubernetes & AKS

Container orchestration with Azure Kubernetes Service (AKS).

## Overview

Kubernetes is a container orchestration platform. AKS is Azure's managed Kubernetes service.

## Key Concepts

### Pod

The smallest deployable unit in Kubernetes. A pod contains one or more containers.

### Deployment

Manages a set of identical pods, ensuring they're running and updated.

### Service

Provides a stable network endpoint to access pods.

### Namespace

Logical separation of resources within a cluster.

## AKS Setup

### Prerequisites

- Azure account
- Azure CLI installed
- kubectl installed

### Create AKS Cluster

```bash
# Login to Azure
az login

# Create resource group
az group create --name myResourceGroup --location eastus

# Create AKS cluster
az aks create \
  --resource-group myResourceGroup \
  --name myAKSCluster \
  --node-count 2 \
  --enable-addons monitoring \
  --generate-ssh-keys

# Get credentials
az aks get-credentials --resource-group myResourceGroup --name myAKSCluster
```

## Basic Kubernetes Objects

### Pod

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: my-pod
spec:
  containers:
  - name: app
    image: my-app:latest
    ports:
    - containerPort: 8080
```

### Deployment

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: my-app
spec:
  replicas: 3
  selector:
    matchLabels:
      app: my-app
  template:
    metadata:
      labels:
        app: my-app
    spec:
      containers:
      - name: app
        image: my-app:latest
        ports:
        - containerPort: 8080
        env:
        - name: DATABASE_URL
          value: "postgresql://db:5432/mydb"
```

### Service

```yaml
apiVersion: v1
kind: Service
metadata:
  name: my-app-service
spec:
  selector:
    app: my-app
  ports:
  - protocol: TCP
    port: 80
    targetPort: 8080
  type: LoadBalancer
```

## Common Commands

### Deploy

```bash
# Apply configuration
kubectl apply -f deployment.yaml

# Get deployments
kubectl get deployments

# Get pods
kubectl get pods

# Get services
kubectl get services
```

### Debugging

```bash
# Describe pod
kubectl describe pod <pod-name>

# View logs
kubectl logs <pod-name>

# Execute command in pod
kubectl exec -it <pod-name> -- /bin/bash

# Port forward
kubectl port-forward <pod-name> 8080:8080
```

### Scaling

```bash
# Scale deployment
kubectl scale deployment my-app --replicas=5

# Auto-scaling
kubectl autoscale deployment my-app --min=2 --max=10 --cpu-percent=80
```

## ConfigMaps and Secrets

### ConfigMap

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: app-config
data:
  database_url: "postgresql://db:5432/mydb"
  log_level: "info"
```

### Secret

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: app-secret
type: Opaque
data:
  password: <base64-encoded-password>
```

### Using in Deployment

```yaml
spec:
  containers:
  - name: app
    image: my-app:latest
    envFrom:
    - configMapRef:
        name: app-config
    - secretRef:
        name: app-secret
```

## Ingress

### Ingress Controller

```bash
# Install NGINX ingress
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.8.1/deploy/static/provider/cloud/deploy.yaml
```

### Ingress Resource

```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: my-app-ingress
spec:
  rules:
  - host: myapp.example.com
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: my-app-service
            port:
              number: 80
```

## Persistent Volumes

### PersistentVolumeClaim

```yaml
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: postgres-pvc
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 10Gi
```

### Using in Pod

```yaml
spec:
  containers:
  - name: postgres
    image: postgres:15
    volumeMounts:
    - name: postgres-storage
      mountPath: /var/lib/postgresql/data
  volumes:
  - name: postgres-storage
    persistentVolumeClaim:
      claimName: postgres-pvc
```

## Health Checks

### Liveness Probe

```yaml
spec:
  containers:
  - name: app
    image: my-app:latest
    livenessProbe:
      httpGet:
        path: /health
        port: 8080
      initialDelaySeconds: 30
      periodSeconds: 10
```

### Readiness Probe

```yaml
readinessProbe:
  httpGet:
    path: /ready
    port: 8080
  initialDelaySeconds: 5
  periodSeconds: 5
```

## Best Practices

1. **Use Deployments** instead of Pods directly
2. **Set resource limits** for containers
3. **Use ConfigMaps** for configuration
4. **Use Secrets** for sensitive data
5. **Implement health checks**
6. **Use namespaces** for organization
7. **Set up monitoring** and logging

## Further Reading

- [Docker](docker.md)
- [CI/CD](cicd.md)
- [GitHub Actions](github-actions.md)

---

*Last updated: {{ git_revision_date_localized }}*
