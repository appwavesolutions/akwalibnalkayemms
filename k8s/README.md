# 🐄 Running Akwal Ibn Al-Kayem MS Locally with Rancher Desktop

This guide will help you run the Akwal Ibn Al-Kayem microservice locally using Rancher Desktop and Kubernetes.

## 📋 Prerequisites

1. **Rancher Desktop** installed and running
2. **kubectl** configured to use Rancher Desktop
3. **Docker** (for building images)
4. **Maven** (for building the application)

## 🚀 Quick Start

### Step 1: Build the Application

```bash
# From the project root directory
./mvnw clean package -DskipTests
```

### Step 2: Build Docker Image

```bash
# Build the Docker image
docker build -t akwalibnalkayemms:local .

# Tag for local registry (Rancher Desktop uses localhost:5000)
docker tag akwalibnalkayemms:local localhost:5000/akwalibnalkayemms:local

# Push to local registry
docker push localhost:5000/akwalibnalkayemms:local
```

### Step 3: Deploy to Kubernetes

#### Option A: Using Kustomize (Recommended)

```bash
# Deploy development environment
kubectl apply -k overlays/dev

# Or deploy test environment
kubectl apply -k overlays/test
```

#### Option B: Using Base Configuration

```bash
# Deploy base configuration
kubectl apply -f base/
```

### Step 4: Access the Application

#### Check Pod Status
```bash
kubectl get pods -n akwalibnalkayemms-dev
```

#### Port Forward (if using base config)
```bash
# Forward port 9090 to localhost
kubectl port-forward service/akwalibnalkayemms-service 9090:9090 -n akwalibnalkayemms-dev
```

#### Access via NodePort
```bash
# Get the NodePort service
kubectl get service akwalibnalkayemms-nodeport -n akwalibnalkayemms-dev

# Access via: http://localhost:30090
```

## 🔧 Configuration Details

### Environment-Specific Deployments

| Environment | Namespace | Replicas | Resources | Database |
|-------------|-----------|----------|-----------|----------|
| **Dev** | `akwalibnalkayemms-dev` | 1 | 128Mi-256Mi | `mysql-dev:3306/akwalibnalkayemms_dev` |
| **Test** | `akwalibnalkayemms-test` | 2 | 256Mi-512Mi | `mysql-test:3306/akwalibnalkayemms_test` |
| **Prod** | `akwalibnalkayemms-prod` | 3 | 512Mi-1Gi | `mysql:3306/akwalibnalkayemms` |

### Database Configuration

The MySQL database is automatically deployed with:
- **Image**: MySQL 8.0
- **Storage**: 10Gi persistent volume
- **Init Scripts**: Loaded from ConfigMap
- **Credentials**: Managed via Kubernetes Secrets

### Application Configuration

- **Port**: 9090
- **Health Checks**: `/actuator/health`
- **Management Endpoints**: `/actuator/info`, `/actuator/metrics`
- **Profiles**: Environment-specific (dev/test/prod)

## 🛠️ Troubleshooting

### Check Application Logs
```bash
# Get pod name
kubectl get pods -n akwalibnalkayemms-dev

# View logs
kubectl logs -f <pod-name> -n akwalibnalkayemms-dev
```

### Check Database Status
```bash
# Check MySQL pod
kubectl get pods -n akwalibnalkayemms-dev | grep mysql

# Check MySQL logs
kubectl logs -f <mysql-pod-name> -n akwalibnalkayemms-dev
```

### Verify Services
```bash
# Check all services
kubectl get services -n akwalibnalkayemms-dev

# Check ingress
kubectl get ingress -n akwalibnalkayemms-dev
```

### Common Issues

1. **Image Pull Errors**: Ensure local registry is running
   ```bash
   # Check if local registry is accessible
   curl http://localhost:5000/v2/_catalog
   ```

2. **Database Connection Issues**: Check if MySQL pod is running and ready
   ```bash
   kubectl get pods -n akwalibnalkayemms-dev
   kubectl describe pod <mysql-pod-name> -n akwalibnalkayemms-dev
   ```

3. **Port Forward Issues**: Try different local ports
   ```bash
   kubectl port-forward service/akwalibnalkayemms-service 8080:9090 -n akwalibnalkayemms-dev
   ```

## 🧹 Cleanup

### Remove Deployment
```bash
# Remove specific environment
kubectl delete -k overlays/dev

# Or remove base configuration
kubectl delete -f base/
```

### Remove Images
```bash
# Remove local images
docker rmi akwalibnalkayemms:local
docker rmi localhost:5000/akwalibnalkayemms:local
```

## 📊 Monitoring

### Health Checks
- **Startup Probe**: `/actuator/health` (30s timeout, 30 failures)
- **Readiness Probe**: `/actuator/health/readiness` (10s interval)
- **Liveness Probe**: `/actuator/health` (30s interval)

### Metrics
- **Prometheus**: `/actuator/prometheus`
- **Application Info**: `/actuator/info`
- **Health Details**: `/actuator/health`

## 🔐 Security Notes

- **Secrets**: Database credentials are stored in Kubernetes Secrets
- **Base64 Encoding**: Current secrets are Base64 encoded (not encrypted)
- **Network**: Services use ClusterIP for internal communication
- **External Access**: Available via NodePort (30090) or Ingress

## 📝 Environment Variables

The application uses the following environment variables:

| Variable | Source | Description |
|----------|--------|-------------|
| `SPRING_PROFILES_ACTIVE` | ConfigMap | Active Spring profile |
| `SPRING_DATASOURCE_URL` | ConfigMap | Database connection URL |
| `SPRING_DATASOURCE_USERNAME` | Secret | Database username |
| `SPRING_DATASOURCE_PASSWORD` | Secret | Database password |
| `SPRING_SECURITY_USER_NAME` | Secret | Security username |
| `SPRING_SECURITY_USER_PASSWORD` | Secret | Security password |

## 🏗️ Java API Deployment Architecture

### How Java API is Deployed to Kubernetes

The Java Spring Boot API follows a containerized deployment pattern using Docker and Kubernetes:

#### 1. **Application Build Process**
- **Source**: Java Spring Boot application in `src/main/java/`
- **Build Tool**: Maven (`pom.xml`)
- **Build Command**: `./mvnw clean package -DskipTests`
- **Output**: JAR file in `target/akwalibnalkayemms-*.jar`

#### 2. **Docker Image Creation**
- **Dockerfile**: Located in project root
- **Base Image**: `eclipse-temurin:17-jre`
- **Security**: Non-root user (`appuser`)
- **Health Check**: Built-in health check using `/actuator/health`
- **JVM Optimization**: Container-aware JVM settings

#### 3. **Kubernetes Deployment Strategy**
- **Kustomize**: Environment-specific configurations
- **Base Config**: Common resources in `base/`
- **Overlays**: Environment-specific patches in `overlays/`
- **Namespaces**: Isolated environments (dev/test/prod)

### Docker Registry Communication

#### Image Push Process
1. **Build**: Docker image built locally or in CI/CD
2. **Tag**: Image tagged for registry (`localhost:5000/akwalibnalkayemms:local`)
3. **Push**: Image pushed to Docker registry
4. **Pull**: Kubernetes pulls image from registry during deployment

#### Registry Types
- **Local Registry**: `localhost:5000` (Rancher Desktop)
- **Production Registry**: External registry (Docker Hub, AWS ECR, etc.)
- **Authentication**: Image pull secrets (`regcred`)

### Jenkins CI/CD Pipeline

#### Pipeline Stages
1. **Checkout**: Git repository checkout
2. **Build**: Maven build with tests
3. **Docker Build**: Create Docker image
4. **Push**: Push to Docker registry
5. **Deploy**: Deploy to Kubernetes
6. **Verify**: Health checks and smoke tests

#### Key Files for CI/CD
- **`scripts/build-and-push.sh`**: Docker build and push script
- **`scripts/deploy-k8s.sh`**: Kubernetes deployment script
- **`scripts/deploy-to-vps.sh`**: VPS deployment script
- **`Dockerfile`**: Container image definition
- **`k8s/`**: Kubernetes manifests

### Kubernetes Resource Files

#### Core Resources
- **`deployment.yaml`**: Application deployment configuration
- **`service.yaml`**: Service definitions (ClusterIP + NodePort)
- **`configmap.yaml`**: Non-sensitive configuration
- **`secret.yaml`**: Sensitive data (credentials)
- **`ingress.yaml`**: External access configuration
- **`mysql-deployment.yaml`**: Database deployment

#### Environment-Specific Files
- **`overlays/dev/`**: Development environment patches
- **`overlays/test/`**: Testing environment patches
- **`overlays/prod/`**: Production environment patches

## 📊 Deployment Architecture Diagram

A comprehensive PlantUML diagram showing the complete CI/CD pipeline and Kubernetes deployment architecture is available in `deployment-architecture.puml`.

### How to View the Diagram

#### Option 1: Online PlantUML Viewer
1. Copy the contents of `deployment-architecture.puml`
2. Go to [PlantUML Online Server](http://www.plantuml.com/plantuml/uml/)
3. Paste the content and view the diagram

#### Option 2: VS Code Extension
1. Install "PlantUML" extension in VS Code
2. Open `deployment-architecture.puml`
3. Use `Alt+D` to preview the diagram

#### Option 3: Local PlantUML
```bash
# Install PlantUML (macOS)
brew install plantuml

# Generate PNG diagram
plantuml deployment-architecture.puml
```

### Diagram Components Explained

#### 🔄 **CI/CD Pipeline Flow**
1. **Git Repository**: Source code, Dockerfile, K8s manifests, scripts
2. **Jenkins Pipeline**: 7-stage automated deployment process
3. **Docker Registry**: Image storage and versioning
4. **Kubernetes Cluster**: Multi-environment deployment

#### 🏗️ **Key Files and Their Roles**

| File/Directory | Purpose | Used By |
|----------------|---------|---------|
| `pom.xml` | Maven build configuration | Jenkins (Maven Build) |
| `Dockerfile` | Container image definition | Jenkins (Docker Build) |
| `k8s/base/` | Base Kubernetes manifests | Jenkins (Deploy to K8s) |
| `k8s/overlays/` | Environment-specific patches | Jenkins (Deploy to K8s) |
| `scripts/build-and-push.sh` | Docker build and push script | Jenkins (Push to Registry) |
| `scripts/deploy-k8s.sh` | Kubernetes deployment script | Jenkins (Deploy to K8s) |

#### 🔐 **Security and Configuration**
- **Secrets**: Database credentials, security passwords
- **ConfigMaps**: Non-sensitive application configuration
- **Image Pull Secrets**: Registry authentication (`regcred`)
- **SSL/TLS**: Ingress controller with Let's Encrypt certificates

#### 📦 **Container and Storage**
- **Docker Images**: Built from `eclipse-temurin:17-jre` base
- **Persistent Storage**: 10Gi PVC for MySQL data
- **Health Checks**: Built-in container health monitoring
- **Resource Limits**: Environment-specific CPU/memory allocation

## 🎯 Next Steps

1. **Customize Configuration**: Modify `overlays/dev/configmap-patch.yaml` for local settings
2. **Add Monitoring**: Set up Prometheus and Grafana for metrics
3. **Implement CI/CD**: Use the existing scripts in `scripts/` directory
4. **Production Deployment**: Use `overlays/prod/` for production environment

---

**Happy Coding! 🚀**
