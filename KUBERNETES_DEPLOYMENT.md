# 🚀 Kubernetes Deployment Guide for Akwalibnalkayemms

This guide will help you deploy the Akwalibnalkayemms application to a Kubernetes VPS server.

## 📋 Prerequisites

### Required Tools
- `kubectl` - Kubernetes command-line tool
- `docker` - Container runtime
- `helm` (optional) - Package manager for Kubernetes
- `kustomize` (optional) - Configuration management

### VPS Requirements
- **Minimum**: 2 CPU cores, 4GB RAM, 20GB storage
- **Recommended**: 4 CPU cores, 8GB RAM, 50GB storage
- **OS**: Ubuntu 20.04+ or CentOS 8+
- **Kubernetes**: v1.20+ (with Ingress Controller)

## 🏗️ Architecture Overview

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Ingress       │    │   Application   │    │   MySQL         │
│   (Nginx)       │───▶│   (Spring Boot) │───▶│   (Database)    │
│   Port 80/443   │    │   Port 9090     │    │   Port 3306     │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## 🔧 Step-by-Step Deployment

### 1. Prepare Your VPS Server

```bash
# Update system
sudo apt update && sudo apt upgrade -y

# Install required packages
sudo apt install -y curl wget git

# Install kubectl
curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
sudo install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl

# Verify installation
kubectl version --client
```

### 2. Configure Kubernetes Cluster

If you don't have a Kubernetes cluster, you can use one of these options:

#### Option A: Using k3s (Recommended for VPS)
```bash
# Install k3s
curl -sfL https://get.k3s.io | sh -

# Configure kubectl
mkdir -p ~/.kube
sudo cp /etc/rancher/k3s/k3s.yaml ~/.kube/config
sudo chown $(id -u):$(id -g) ~/.kube/config

# Verify cluster
kubectl get nodes
```

#### Option B: Using MicroK8s
```bash
# Install MicroK8s
sudo snap install microk8s --classic

# Add user to microk8s group
sudo usermod -a -G microk8s $USER
newgrp microk8s

# Enable required addons
microk8s enable dns storage ingress

# Configure kubectl
microk8s kubectl config view --raw > ~/.kube/config
```

### 3. Install Ingress Controller

```bash
# Install NGINX Ingress Controller
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.8.2/deploy/static/provider/cloud/deploy.yaml

# Wait for ingress controller to be ready
kubectl wait --namespace ingress-nginx \
  --for=condition=ready pod \
  --selector=app.kubernetes.io/component=controller \
  --timeout=120s
```

### 4. Install cert-manager (for SSL/TLS)

```bash
# Install cert-manager
kubectl apply -f https://github.com/cert-manager/cert-manager/releases/download/v1.13.0/cert-manager.yaml

# Wait for cert-manager to be ready
kubectl wait --namespace cert-manager \
  --for=condition=ready pod \
  --selector=app.kubernetes.io/component=controller \
  --timeout=120s
```

### 5. Build and Push Docker Image

```bash
# Update registry in build script
nano scripts/build-and-push.sh
# Change REGISTRY="your-registry.com" to your actual registry

# Build and push image
chmod +x scripts/build-and-push.sh
./scripts/build-and-push.sh v1.0.0 prod

# Or if using Docker Hub
docker build -t yourusername/akwalibnalkayemms:latest .
docker push yourusername/akwalibnalkayemms:latest
```

### 6. Update Image References

Update the following files with your actual image registry:

```bash
# Update k8s/base/deployment.yaml
sed -i 's|akwalibnalkayemms:latest|yourusername/akwalibnalkayemms:latest|g' k8s/base/deployment.yaml

# Update helm values if using Helm
sed -i 's|akwalibnalkayemms|yourusername/akwalibnalkayemms|g' helm/akwalibnalkayemms/values*.yaml
```

### 7. Configure Secrets

```bash
# Create namespace
kubectl create namespace akwalibnalkayemms-prod

# Update secrets with your actual values
kubectl create secret generic akwalibnalkayemms-secrets \
  --from-literal=spring.datasource.username=appuser \
  --from-literal=spring.datasource.password=your_secure_password \
  --from-literal=spring.security.user.name=admin \
  --from-literal=spring.security.user.password=your_admin_password \
  -n akwalibnalkayemms-prod

# Create MySQL secrets
kubectl create secret generic mysql-secrets \
  --from-literal=mysql-root-password=your_mysql_root_password \
  --from-literal=mysql-username=appuser \
  --from-literal=mysql-password=your_mysql_password \
  -n akwalibnalkayemms-prod
```

### 8. Deploy Application

#### Option A: Using Kustomize (Recommended)
```bash
# Deploy MySQL first
kubectl apply -f k8s/base/mysql-deployment.yaml -n akwalibnalkayemms-prod

# Wait for MySQL to be ready
kubectl wait --for=condition=ready pod -l app=mysql -n akwalibnalkayemms-prod --timeout=300s

# Deploy application
kubectl apply -f k8s/base/ -n akwalibnalkayemms-prod

# Check deployment status
kubectl get pods -n akwalibnalkayemms-prod
kubectl get services -n akwalibnalkayemms-prod
```

#### Option B: Using Helm
```bash
# Add Helm repository (if needed)
helm repo add stable https://charts.helm.sh/stable

# Deploy with Helm
helm install akwalibnalkayemms ./helm/akwalibnalkayemms \
  --namespace akwalibnalkayemms-prod \
  --values ./helm/akwalibnalkayemms/values-prod.yaml \
  --set image.repository=yourusername/akwalibnalkayemms \
  --set image.tag=latest
```

### 9. Configure Ingress

```bash
# Update domain in ingress configuration
sed -i 's/akwalibnalkayemms.yourdomain.com/your-actual-domain.com/g' k8s/base/ingress-prod.yaml

# Deploy ingress
kubectl apply -f k8s/base/ingress-prod.yaml -n akwalibnalkayemms-prod

# Check ingress status
kubectl get ingress -n akwalibnalkayemms-prod
```

### 10. Verify Deployment

```bash
# Check all resources
kubectl get all -n akwalibnalkayemms-prod

# Check pod logs
kubectl logs -l app=akwalibnalkayemms -n akwalibnalkayemms-prod

# Check ingress
kubectl describe ingress akwalibnalkayemms-ingress-prod -n akwalibnalkayemms-prod

# Test application
curl -k https://your-actual-domain.com/api/quotes
```

## 🔍 Monitoring and Health Checks

### Health Endpoints
- **Health Check**: `https://your-domain.com/actuator/health`
- **Info**: `https://your-domain.com/actuator/info`
- **Metrics**: `https://your-domain.com/actuator/metrics`
- **Swagger UI**: `https://your-domain.com/swagger-ui.html`

### Monitoring Commands
```bash
# Check pod status
kubectl get pods -n akwalibnalkayemms-prod -w

# Check logs
kubectl logs -f deployment/akwalibnalkayemms -n akwalibnalkayemms-prod

# Check resource usage
kubectl top pods -n akwalibnalkayemms-prod

# Check events
kubectl get events -n akwalibnalkayemms-prod --sort-by='.lastTimestamp'
```

## 🔧 Troubleshooting

### Common Issues

#### 1. Pod Not Starting
```bash
# Check pod status
kubectl describe pod <pod-name> -n akwalibnalkayemms-prod

# Check logs
kubectl logs <pod-name> -n akwalibnalkayemms-prod
```

#### 2. Database Connection Issues
```bash
# Check MySQL pod
kubectl get pods -l app=mysql -n akwalibnalkayemms-prod

# Check MySQL logs
kubectl logs -l app=mysql -n akwalibnalkayemms-prod

# Test database connection
kubectl exec -it <mysql-pod> -n akwalibnalkayemms-prod -- mysql -u root -p
```

#### 3. Ingress Issues
```bash
# Check ingress controller
kubectl get pods -n ingress-nginx

# Check ingress status
kubectl describe ingress akwalibnalkayemms-ingress-prod -n akwalibnalkayemms-prod

# Check ingress controller logs
kubectl logs -n ingress-nginx -l app.kubernetes.io/name=ingress-nginx
```

## 🔄 Updates and Maintenance

### Rolling Updates
```bash
# Update application
kubectl set image deployment/akwalibnalkayemms akwalibnalkayemms=yourusername/akwalibnalkayemms:v1.1.0 -n akwalibnalkayemms-prod

# Check rollout status
kubectl rollout status deployment/akwalibnalkayemms -n akwalibnalkayemms-prod

# Rollback if needed
kubectl rollout undo deployment/akwalibnalkayemms -n akwalibnalkayemms-prod
```

### Scaling
```bash
# Scale application
kubectl scale deployment akwalibnalkayemms --replicas=3 -n akwalibnalkayemms-prod

# Enable horizontal pod autoscaling
kubectl autoscale deployment akwalibnalkayemms --cpu-percent=70 --min=2 --max=10 -n akwalibnalkayemms-prod
```

### Backup
```bash
# Backup MySQL data
kubectl exec -it <mysql-pod> -n akwalibnalkayemms-prod -- mysqldump -u root -p akwalibnalkayemms > backup.sql

# Backup Kubernetes resources
kubectl get all -n akwalibnalkayemms-prod -o yaml > akwalibnalkayemms-backup.yaml
```

## 🛡️ Security Considerations

1. **Use strong passwords** for all secrets
2. **Enable RBAC** for Kubernetes access
3. **Use network policies** to restrict traffic
4. **Regular security updates** for base images
5. **Monitor logs** for suspicious activity
6. **Use HTTPS** for all external access

## 📊 Performance Optimization

1. **Resource limits** are configured in deployment
2. **Horizontal Pod Autoscaling** is available
3. **Database connection pooling** is configured
4. **Caching** can be added with Redis
5. **CDN** can be used for static content

## 🆘 Support

If you encounter issues:
1. Check the troubleshooting section above
2. Review Kubernetes logs
3. Verify all configurations
4. Check resource availability
5. Ensure network connectivity

---

**Note**: Replace all placeholder values (your-registry.com, your-domain.com, etc.) with your actual values before deployment.
