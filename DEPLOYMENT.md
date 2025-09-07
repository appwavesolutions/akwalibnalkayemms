# Deployment Guide

This guide covers deploying the Akwalibnalkayemms microservice using Docker and Kubernetes (K3s) across different environments.

## 🏗️ Architecture Overview

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Load Balancer │    │   Ingress       │    │   Application   │
│   (Nginx)       │───▶│   Controller    │───▶│   Pods          │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                                        │
                       ┌─────────────────┐              │
                       │   MySQL         │◀─────────────┘
                       │   Database      │
                       └─────────────────┘
```

## 📋 Prerequisites

### Local Development
- Docker & Docker Compose
- Java 17+
- Maven 3.6+

### Server Deployment
- K3s cluster running
- kubectl configured
- Helm 3.x (optional)
- Docker registry access

## 🚀 Local Development

### Using Docker Compose

1. **Start the application with database:**
   ```bash
   docker-compose up -d
   ```

2. **Access the application:**
   - API: http://localhost:9090
   - Swagger UI: http://localhost:9090/swagger-ui.html
   - Nginx Proxy: http://localhost:80

3. **View logs:**
   ```bash
   docker-compose logs -f app
   ```

4. **Stop the application:**
   ```bash
   docker-compose down
   ```

### Development with Hot Reload

1. **Start only the database:**
   ```bash
   docker-compose up -d mysql
   ```

2. **Run the application locally:**
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=dev
   ```

## 🐳 Docker Deployment

### Build Docker Image

```bash
# Build the image
docker build -t akwalibnalkayemms:latest .

# Tag for registry
docker tag akwalibnalkayemms:latest your-registry.com/akwalibnalkayemms:latest

# Push to registry
docker push your-registry.com/akwalibnalkayemms:latest
```

### Using Build Script

```bash
# Build and push for different environments
./scripts/build-and-push.sh v1.0.0 dev
./scripts/build-and-push.sh v1.0.0 test
./scripts/build-and-push.sh v1.0.0 prod
```

## ☸️ Kubernetes Deployment

### Method 1: Using Kustomize (Recommended)

#### Deploy to Development
```bash
# Create namespace and deploy
kubectl create namespace akwalibnalkayemms-dev
kubectl apply -k k8s/overlays/dev

# Check deployment status
kubectl get pods -n akwalibnalkayemms-dev
kubectl get services -n akwalibnalkayemms-dev
```

#### Deploy to Test
```bash
kubectl create namespace akwalibnalkayemms-test
kubectl apply -k k8s/overlays/test
```

#### Deploy to Production
```bash
kubectl create namespace akwalibnalkayemms-prod
kubectl apply -k k8s/overlays/prod
```

### Method 2: Using Helm

#### Install Helm Chart
```bash
# Add Helm repository (if using a chart repository)
helm repo add akwalibnalkayemms ./helm/akwalibnalkayemms

# Install for development
helm install akwalibnalkayemms-dev ./helm/akwalibnalkayemms \
  --namespace akwalibnalkayemms-dev \
  --create-namespace \
  --values ./helm/akwalibnalkayemms/values-dev.yaml

# Install for test
helm install akwalibnalkayemms-test ./helm/akwalibnalkayemms \
  --namespace akwalibnalkayemms-test \
  --create-namespace \
  --values ./helm/akwalibnalkayemms/values-test.yaml

# Install for production
helm install akwalibnalkayemms-prod ./helm/akwalibnalkayemms \
  --namespace akwalibnalkayemms-prod \
  --create-namespace \
  --values ./helm/akwalibnalkayemms/values-prod.yaml
```

### Method 3: Using Deployment Scripts

```bash
# Deploy to different environments
./scripts/deploy-k8s.sh dev
./scripts/deploy-k8s.sh test
./scripts/deploy-k8s.sh prod

# Deploy with specific image tag
./scripts/deploy-k8s.sh prod v1.0.0
```

## 🔧 Configuration

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `SPRING_PROFILES_ACTIVE` | Active Spring profile | `prod` |
| `SPRING_DATASOURCE_URL` | Database connection URL | - |
| `SPRING_DATASOURCE_USERNAME` | Database username | - |
| `SPRING_DATASOURCE_PASSWORD` | Database password | - |
| `SPRING_SECURITY_USER_NAME` | Basic auth username | `admin` |
| `SPRING_SECURITY_USER_PASSWORD` | Basic auth password | `secret123` |

### Database Configuration

#### Development
- **Host**: `mysql-dev` (K8s) or `localhost` (Docker)
- **Database**: `akwalibnalkayemms_dev`
- **SSL**: Disabled

#### Test
- **Host**: `mysql-test` (K8s) or `localhost` (Docker)
- **Database**: `akwalibnalkayemms_test`
- **SSL**: Disabled

#### Production
- **Host**: `mysql-prod` (K8s)
- **Database**: `akwalibnalkayemms_prod`
- **SSL**: Enabled

## 🌐 Accessing the Application

### Local Development
- **Direct**: http://localhost:9090
- **Via Nginx**: http://localhost:80
- **Swagger UI**: http://localhost:9090/swagger-ui.html

### Kubernetes (NodePort)
- **Development**: http://your-server:30090
- **Test**: http://your-server:30090
- **Production**: Via Ingress (configure your domain)

### Kubernetes (Ingress)
- **Development**: http://akwalibnalkayemms-dev.local
- **Test**: http://akwalibnalkayemms-test.local
- **Production**: https://akwalibnalkayemms.yourdomain.com

## 📊 Monitoring and Health Checks

### Health Endpoints
- **Liveness**: `/actuator/health`
- **Readiness**: `/actuator/health/readiness`
- **Info**: `/actuator/info`
- **Metrics**: `/actuator/metrics`

### Monitoring Commands
```bash
# Check pod status
kubectl get pods -n akwalibnalkayemms-{env}

# Check service status
kubectl get services -n akwalibnalkayemms-{env}

# Check ingress status
kubectl get ingress -n akwalibnalkayemms-{env}

# View logs
kubectl logs -f deployment/akwalibnalkayemms -n akwalibnalkayemms-{env}

# Port forward for local access
kubectl port-forward service/akwalibnalkayemms-service 9090:9090 -n akwalibnalkayemms-{env}
```

## 🔄 Updates and Rollbacks

### Update Deployment
```bash
# Update image tag
kubectl set image deployment/akwalibnalkayemms akwalibnalkayemms=akwalibnalkayemms:v1.1.0 -n akwalibnalkayemms-{env}

# Or using Helm
helm upgrade akwalibnalkayemms-{env} ./helm/akwalibnalkayemms \
  --namespace akwalibnalkayemms-{env} \
  --set image.tag=v1.1.0
```

### Rollback
```bash
# Rollback to previous version
kubectl rollout undo deployment/akwalibnalkayemms -n akwalibnalkayemms-{env}

# Or using Helm
helm rollback akwalibnalkayemms-{env} 1 -n akwalibnalkayemms-{env}
```

## 🗄️ Database Setup

### MySQL Database Creation
```sql
-- Create databases for each environment
CREATE DATABASE akwalibnalkayemms_dev;
CREATE DATABASE akwalibnalkayemms_test;
CREATE DATABASE akwalibnalkayemms_prod;

-- Create users (optional)
CREATE USER 'akwalibnalkayemms_dev'@'%' IDENTIFIED BY 'password';
CREATE USER 'akwalibnalkayemms_test'@'%' IDENTIFIED BY 'password';
CREATE USER 'akwalibnalkayemms_prod'@'%' IDENTIFIED BY 'password';

-- Grant permissions
GRANT ALL PRIVILEGES ON akwalibnalkayemms_dev.* TO 'akwalibnalkayemms_dev'@'%';
GRANT ALL PRIVILEGES ON akwalibnalkayemms_test.* TO 'akwalibnalkayemms_test'@'%';
GRANT ALL PRIVILEGES ON akwalibnalkayemms_prod.* TO 'akwalibnalkayemms_prod'@'%';
FLUSH PRIVILEGES;
```

### Initialize Data
```bash
# Run SQL scripts to initialize data
mysql -h your-mysql-host -u root -p akwalibnalkayemms_dev < sql/ddl.sql
mysql -h your-mysql-host -u root -p akwalibnalkayemms_dev < sql/data_book.sql
mysql -h your-mysql-host -u root -p akwalibnalkayemms_dev < sql/data_chapter.sql
mysql -h your-mysql-host -u root -p akwalibnalkayemms_dev < sql/data_quote.sql
# ... and other data files
```

## 🔒 Security Considerations

### Secrets Management
```bash
# Create secrets for each environment
kubectl create secret generic akwalibnalkayemms-secrets \
  --from-literal=spring.datasource.username=your-username \
  --from-literal=spring.datasource.password=your-password \
  --from-literal=spring.security.user.name=admin \
  --from-literal=spring.security.user.password=your-secure-password \
  -n akwalibnalkayemms-{env}
```

### SSL/TLS Configuration
- Configure cert-manager for automatic SSL certificate management
- Use Let's Encrypt for production certificates
- Enable HTTPS redirects in production

## 🚨 Troubleshooting

### Common Issues

1. **Pod not starting:**
   ```bash
   kubectl describe pod <pod-name> -n akwalibnalkayemms-{env}
   kubectl logs <pod-name> -n akwalibnalkayemms-{env}
   ```

2. **Database connection issues:**
   - Check database credentials in secrets
   - Verify database URL configuration
   - Ensure database is accessible from pods

3. **Ingress not working:**
   - Check ingress controller is running
   - Verify DNS configuration
   - Check ingress annotations

4. **Health checks failing:**
   - Verify application is starting correctly
   - Check resource limits
   - Review application logs

### Debug Commands
```bash
# Get all resources
kubectl get all -n akwalibnalkayemms-{env}

# Describe resources
kubectl describe deployment akwalibnalkayemms -n akwalibnalkayemms-{env}

# Check events
kubectl get events -n akwalibnalkayemms-{env} --sort-by='.lastTimestamp'

# Port forward for debugging
kubectl port-forward service/akwalibnalkayemms-service 9090:9090 -n akwalibnalkayemms-{env}
```

## 📈 Scaling

### Horizontal Pod Autoscaling
```bash
# Enable autoscaling (already configured in production values)
kubectl autoscale deployment akwalibnalkayemms \
  --cpu-percent=70 \
  --min=3 \
  --max=10 \
  -n akwalibnalkayemms-prod
```

### Manual Scaling
```bash
# Scale deployment
kubectl scale deployment akwalibnalkayemms --replicas=5 -n akwalibnalkayemms-{env}
```

## 🧹 Cleanup

### Remove Deployment
```bash
# Using Kustomize
kubectl delete -k k8s/overlays/{env}

# Using Helm
helm uninstall akwalibnalkayemms-{env} -n akwalibnalkayemms-{env}

# Remove namespace
kubectl delete namespace akwalibnalkayemms-{env}
```

### Clean Docker Images
```bash
# Remove local images
docker rmi akwalibnalkayemms:latest
docker system prune -a
```

---

For more detailed information, refer to the [README.md](README.md) file.
