#!/bin/bash

# Build and push Docker image script
set -e

# Configuration
IMAGE_NAME="akwalibnalkayemms"
REGISTRY="your-registry.com"  # Replace with your registry
VERSION=${1:-latest}
ENVIRONMENT=${2:-dev}

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}Building and pushing Docker image...${NC}"

# Build the image
echo -e "${YELLOW}Building Docker image: ${IMAGE_NAME}:${VERSION}${NC}"
docker build -t ${IMAGE_NAME}:${VERSION} .

# Tag for registry
docker tag ${IMAGE_NAME}:${VERSION} ${REGISTRY}/${IMAGE_NAME}:${VERSION}
docker tag ${IMAGE_NAME}:${VERSION} ${REGISTRY}/${IMAGE_NAME}:${ENVIRONMENT}

# Push to registry
echo -e "${YELLOW}Pushing to registry...${NC}"
docker push ${REGISTRY}/${IMAGE_NAME}:${VERSION}
docker push ${REGISTRY}/${IMAGE_NAME}:${ENVIRONMENT}

echo -e "${GREEN}Successfully built and pushed:${NC}"
echo -e "  - ${REGISTRY}/${IMAGE_NAME}:${VERSION}"
echo -e "  - ${REGISTRY}/${IMAGE_NAME}:${ENVIRONMENT}"
