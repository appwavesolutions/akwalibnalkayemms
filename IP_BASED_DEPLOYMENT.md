# 🌐 IP-Based Kubernetes Deployment Guide

This guide will help you deploy the Akwalibnalkayemms application to a Kubernetes VPS server using IP address instead of domain name.

## 📋 Prerequisites

### VPS Requirements
- **IP Address**: 147.79.100.189 (your VPS IP)
- **Minimum**: 2 CPU cores, 4GB RAM, 20GB storage
- **Recommended**: 4 CPU cores, 8GB RAM, 50GB storage
- **OS**: Ubuntu 20.04+ or CentOS 8+
- **Kubernetes**: v1.20+ (with Ingress Controller)

### Required Tools
- `kubectl` - Kubernetes command-line tool
- `docker` - Container runtime
- Docker Registry running on port 5000

## 🏗️ Architecture Overview

```
Internet → Ingress (Port 80) → Spring Boot App → MySQL Database
    ↓           ↓              ↓              ↓
  Port 80    Port 9090     Port 9090      Port 3306
```

## 🚀 Quick Deployment

### Option 1: One-Command Deployment
```bash
# Deploy with default settings
./scripts/deploy-ip-based.sh

# Deploy with custom image tag
./scripts/deploy-ip-based.sh v1.0.0
```

### Option 2: Manual Step-by-Step
```bash
# 1. Build and push image to your registry
docker build -t 147.79.100.189:5000/akwalibnalkayemms:latest .
docker push 147.79.100.189:5000/akwalibnalkayemms:latest

# 2. Create namespace
kubectl create namespace akwalibnalkayemms-prod

# 3. Deploy MySQL
kubectl apply -f k8s/base/mysql-deployment.yaml -n akwalibnalkayemms-prod

# 4. Deploy application
kubectl apply -f k8s/base/ -n akwalibnalkayemms-prod

# 5. Deploy IP-based ingress
kubectl apply -f k8s/base/ingress-ip.yaml -n akwalibnalkayemms-prod
```

## 🔧 Configuration Details

### Docker Registry Setup
Your registry is configured at `147.79.100.189:5000`. Make sure it's running:

```bash
# Start Docker registry (if not already running)
docker run -d -p 5000:5000 --name registry registry:2

# Verify registry is accessible
curl http://147.79.100.189:5000/v2/
```

### Ingress Configuration
- **No SSL/TLS**: HTTP only (port 80)
- **Host**: 147.79.100.189
- **CORS**: Enabled for all origins
- **Rate Limiting**: 100 requests per minute

### Application Access
- **Main Application**: http://147.79.100.189
- **Swagger UI**: http://147.79.100.189/swagger-ui.html
- **Health Check**: http://147.79.100.189/actuator/health
- **API Base**: http://147.79.100.189/api

## 🔍 Verification

### Check Deployment Status
```bash
# Check all resources
kubectl get all -n akwalibnalkayemms-prod

# Check pods
kubectl get pods -n akwalibnalkayemms-prod

# Check services
kubectl get services -n akwalibnalkayemms-prod

# Check ingress
kubectl get ingress -n akwalibnalkayemms-prod
```

### Test Application
```bash
# Test health endpoint
curl http://147.79.100.189/actuator/health

# Test API endpoint
curl http://147.79.100.189/api/quotes

# Test with authentication
curl -u admin:secret123 http://147.79.100.189/api/quotes
```

## 🔧 Troubleshooting

### Common Issues

#### 1. Registry Connection Issues
```bash
# Check if registry is accessible
curl http://147.79.100.189:5000/v2/

# Check if image exists
curl http://147.79.100.189:5000/v2/akwalibnalkayemms/tags/list
```

#### 2. Pod Not Starting
```bash
# Check pod status
kubectl describe pod <pod-name> -n akwalibnalkayemms-prod

# Check logs
kubectl logs <pod-name> -n akwalibnalkayemms-prod
```

#### 3. Ingress Issues
```bash
# Check ingress controller
kubectl get pods -n ingress-nginx

# Check ingress status
kubectl describe ingress akwalibnalkayemms-ingress-ip -n akwalibnalkayemms-prod

# Check ingress controller logs
kubectl logs -n ingress-nginx -l app.kubernetes.io/name=ingress-nginx
```

#### 4. Database Connection Issues
```bash
# Check MySQL pod
kubectl get pods -l app=mysql -n akwalibnalkayemms-prod

# Check MySQL logs
kubectl logs -l app=mysql -n akwalibnalkayemms-prod

# Test database connection
kubectl exec -it <mysql-pod> -n akwalibnalkayemms-prod -- mysql -u root -p
```

## 🔄 Updates and Maintenance

### Rolling Updates
```bash
# Update application
kubectl set image deployment/akwalibnalkayemms akwalibnalkayemms=147.79.100.189:5000/akwalibnalkayemms:v1.1.0 -n akwalibnalkayemms-prod

# Check rollout status
kubectl rollout status deployment/akwalibnalkayemms -n akwalibnalkayemms-prod

# Rollback if needed
kubectl rollout undo deployment/akwalibnalkayemms -n akwalibnalkayemms-prod
```

### Scaling
```bash
# Scale application
kubectl scale deployment akwalibnalkayemms --replicas=3 -n akwalibnalkayemms-prod

# Check scaling
kubectl get pods -n akwalibnalkayemms-prod
```

### Monitoring
```bash
# Check resource usage
kubectl top pods -n akwalibnalkayemms-prod

# Check events
kubectl get events -n akwalibnalkayemms-prod --sort-by='.lastTimestamp'

# Follow logs
kubectl logs -f deployment/akwalibnalkayemms -n akwalibnalkayemms-prod
```

## 🛡️ Security Considerations

1. **No SSL/TLS**: This setup uses HTTP only. For production, consider:
   - Using a reverse proxy with SSL termination
   - Setting up Let's Encrypt with a domain name
   - Using a load balancer with SSL

2. **Network Security**:
   - Ensure only necessary ports are open
   - Use firewall rules to restrict access
   - Consider VPN access for admin functions

3. **Application Security**:
   - Change default passwords
   - Use strong database passwords
   - Enable RBAC for Kubernetes access

## 📊 Performance Optimization

1. **Resource Limits**: Configured in deployment
2. **Horizontal Pod Autoscaling**: Available
3. **Database Connection Pooling**: Configured
4. **Caching**: Can be added with Redis

## 🆘 Support

If you encounter issues:
1. Check the troubleshooting section above
2. Review Kubernetes logs
3. Verify all configurations
4. Check resource availability
5. Ensure network connectivity

## 📝 Quick Reference

### Access URLs
- **Application**: http://147.79.100.189
- **Swagger UI**: http://147.79.100.189/swagger-ui.html
- **Health Check**: http://147.79.100.189/actuator/health
- **API Base**: http://147.79.100.189/api

### Default Credentials
- **Admin User**: admin
- **Admin Password**: secret123
- **Database User**: appuser
- **Database Password**: apppass123

### Useful Commands
```bash
# Check everything
kubectl get all -n akwalibnalkayemms-prod

# Check logs
kubectl logs -f deployment/akwalibnalkayemms -n akwalibnalkayemms-prod

# Scale app
kubectl scale deployment akwalibnalkayemms --replicas=3 -n akwalibnalkayemms-prod

# Delete everything
kubectl delete namespace akwalibnalkayemms-prod
```

---

**Note**: This setup is optimized for IP-based access without domain names. For production use with SSL/TLS, consider obtaining a domain name and updating the configuration accordingly.
