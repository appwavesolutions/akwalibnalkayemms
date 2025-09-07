#!/bin/bash

# Quick deployment script for VPS Kubernetes deployment
set -e

# Configuration
NAMESPACE="akwalibnalkayemms-prod"
DOMAIN=${1:-"147.79.100.189"}
REGISTRY=${2:-"147.79.100.189:5000"}
IMAGE_TAG=${3:-"latest"}

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}🚀 Starting Akwalibnalkayemms VPS Deployment${NC}"
echo -e "${YELLOW}Domain: ${DOMAIN}${NC}"
echo -e "${YELLOW}Registry: ${REGISTRY}${NC}"
echo -e "${YELLOW}Image Tag: ${IMAGE_TAG}${NC}"

# Check prerequisites
echo -e "${YELLOW}Checking prerequisites...${NC}"

if ! command -v kubectl &> /dev/null; then
    echo -e "${RED}kubectl is not installed${NC}"
    exit 1
fi

if ! command -v docker &> /dev/null; then
    echo -e "${RED}docker is not installed${NC}"
    exit 1
fi

# Check if cluster is accessible
if ! kubectl cluster-info &> /dev/null; then
    echo -e "${RED}Kubernetes cluster is not accessible${NC}"
    exit 1
fi

echo -e "${GREEN}✅ Prerequisites check passed${NC}"

# Create namespace
echo -e "${YELLOW}Creating namespace: ${NAMESPACE}${NC}"
kubectl create namespace ${NAMESPACE} --dry-run=client -o yaml | kubectl apply -f -

# Build and push Docker image
echo -e "${YELLOW}Building and pushing Docker image...${NC}"
docker build -t ${REGISTRY}/akwalibnalkayemms:${IMAGE_TAG} .
docker push ${REGISTRY}/akwalibnalkayemms:${IMAGE_TAG}

# Update image references
echo -e "${YELLOW}Updating image references...${NC}"
sed -i "s|akwalibnalkayemms:latest|${REGISTRY}/akwalibnalkayemms:${IMAGE_TAG}|g" k8s/base/deployment.yaml
sed -i "s|akwalibnalkayemms.yourdomain.com|${DOMAIN}|g" k8s/base/ingress-prod.yaml
sed -i "s|akwalibnalkayemms.yourdomain.com|${DOMAIN}|g" k8s/overlays/prod/ingress-patch.yaml

# Create secrets
echo -e "${YELLOW}Creating secrets...${NC}"
kubectl create secret generic akwalibnalkayemms-secrets \
  --from-literal=spring.datasource.username=appuser \
  --from-literal=spring.datasource.password=apppass123 \
  --from-literal=spring.security.user.name=admin \
  --from-literal=spring.security.user.password=secret123 \
  -n ${NAMESPACE} --dry-run=client -o yaml | kubectl apply -f -

kubectl create secret generic mysql-secrets \
  --from-literal=mysql-root-password=admin123 \
  --from-literal=mysql-username=appuser \
  --from-literal=mysql-password=apppass123 \
  -n ${NAMESPACE} --dry-run=client -o yaml | kubectl apply -f -

# Deploy MySQL first
echo -e "${YELLOW}Deploying MySQL...${NC}"
kubectl apply -f k8s/base/mysql-deployment.yaml -n ${NAMESPACE}

# Wait for MySQL to be ready
echo -e "${YELLOW}Waiting for MySQL to be ready...${NC}"
kubectl wait --for=condition=ready pod -l app=mysql -n ${NAMESPACE} --timeout=300s

# Deploy application
echo -e "${YELLOW}Deploying application...${NC}"
kubectl apply -f k8s/base/configmap.yaml -n ${NAMESPACE}
kubectl apply -f k8s/base/secret.yaml -n ${NAMESPACE}
kubectl apply -f k8s/base/deployment.yaml -n ${NAMESPACE}
kubectl apply -f k8s/base/service.yaml -n ${NAMESPACE}

# Wait for application to be ready
echo -e "${YELLOW}Waiting for application to be ready...${NC}"
kubectl wait --for=condition=ready pod -l app=akwalibnalkayemms -n ${NAMESPACE} --timeout=300s

# Deploy ingress
echo -e "${YELLOW}Deploying ingress...${NC}"
kubectl apply -f k8s/base/ingress-prod.yaml -n ${NAMESPACE}

# Get deployment status
echo -e "${GREEN}✅ Deployment completed successfully!${NC}"
echo -e "${BLUE}📊 Deployment Status:${NC}"
kubectl get pods -n ${NAMESPACE}
echo -e "${BLUE}🌐 Services:${NC}"
kubectl get services -n ${NAMESPACE}
echo -e "${BLUE}🔗 Ingress:${NC}"
kubectl get ingress -n ${NAMESPACE}

# Show access information
echo -e "${GREEN}🎉 Application is now accessible at:${NC}"
echo -e "${GREEN}   http://${DOMAIN}${NC}"
echo -e "${GREEN}   Swagger UI: http://${DOMAIN}/swagger-ui.html${NC}"
echo -e "${GREEN}   Health Check: http://${DOMAIN}/actuator/health${NC}"

# Show useful commands
echo -e "${BLUE}📝 Useful Commands:${NC}"
echo -e "${YELLOW}   Check logs: kubectl logs -f deployment/akwalibnalkayemms -n ${NAMESPACE}${NC}"
echo -e "${YELLOW}   Check status: kubectl get all -n ${NAMESPACE}${NC}"
echo -e "${YELLOW}   Scale app: kubectl scale deployment akwalibnalkayemms --replicas=3 -n ${NAMESPACE}${NC}"
echo -e "${YELLOW}   Delete all: kubectl delete namespace ${NAMESPACE}${NC}"
