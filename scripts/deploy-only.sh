#!/bin/bash

# Kubernetes deployment script
set -e

# Configuration
ENVIRONMENT=${1:-dev}
NAMESPACE="akwalibnalkayemms-${ENVIRONMENT}"
IMAGE_TAG=${2:-latest}

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}Deploying to Kubernetes environment: ${ENVIRONMENT}${NC}"

# Check if kubectl is available
if ! command -v kubectl &> /dev/null; then
    echo -e "${RED}kubectl is not installed or not in PATH${NC}"
    exit 1
fi

# Check if kustomize is available
if ! command -v kustomize &> /dev/null; then
    echo -e "${YELLOW}kustomize not found, using kubectl kustomize${NC}"
    KUSTOMIZE_CMD="kubectl kustomize"
else
    KUSTOMIZE_CMD="kustomize build"
fi

# Create namespace if it doesn't exist
echo -e "${YELLOW}Creating namespace: ${NAMESPACE}${NC}"
kubectl create namespace ${NAMESPACE} --dry-run=client -o yaml | kubectl apply -f -

# Apply secrets (you need to update these with your actual values)
echo -e "${YELLOW}Applying secrets...${NC}"
kubectl apply -f k8s/base/secret.yaml -n ${NAMESPACE}

# Build and apply kustomization
echo -e "${YELLOW}Building and applying kustomization for ${ENVIRONMENT}...${NC}"
${KUSTOMIZE_CMD} k8s/overlays/${ENVIRONMENT} | kubectl apply -f -

# Update image tag if provided
if [ "$IMAGE_TAG" != "latest" ]; then
    echo -e "${YELLOW}Updating image tag to: ${IMAGE_TAG}${NC}"
    kubectl set image deployment/akwalibnalkayemms akwalibnalkayemms=akwalibnalkayemms:${IMAGE_TAG} -n ${NAMESPACE}
fi

# Wait for deployment to be ready
echo -e "${YELLOW}Waiting for deployment to be ready...${NC}"
kubectl rollout status deployment/akwalibnalkayemms -n ${NAMESPACE} --timeout=300s

# Get service information
echo -e "${GREEN}Deployment completed successfully!${NC}"
echo -e "${BLUE}Service information:${NC}"
kubectl get services -n ${NAMESPACE}
echo -e "${BLUE}Pods status:${NC}"
kubectl get pods -n ${NAMESPACE}

# Show access information
if [ "$ENVIRONMENT" = "dev" ] || [ "$ENVIRONMENT" = "test" ]; then
    NODEPORT=$(kubectl get service akwalibnalkayemms-nodeport -n ${NAMESPACE} -o jsonpath='{.spec.ports[0].nodePort}')
    echo -e "${GREEN}Access the application at: http://localhost:${NODEPORT}${NC}"
    echo -e "${GREEN}Swagger UI: http://localhost:${NODEPORT}/swagger-ui.html${NC}"
fi
