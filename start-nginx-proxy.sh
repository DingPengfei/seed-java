#!/bin/bash

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}🚀 Starting Seed IoT Platform with Nginx Proxy${NC}"

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo -e "${RED}❌ Docker is not running. Please start Docker first.${NC}"
    exit 1
fi

# Check if docker-compose is available
if ! command -v docker-compose &> /dev/null; then
    echo -e "${YELLOW}⚠️  docker-compose not found, trying docker compose...${NC}"
    DOCKER_COMPOSE="docker compose"
else
    DOCKER_COMPOSE="docker-compose"
fi

echo -e "${BLUE}📋 Services to be started:${NC}"
echo -e "  • Nginx Proxy (port 80)"
echo -e "  • EMQX MQTT Broker (ports 1883, 8083, 18083)"
echo -e "  • Redis Cache (port 6379)"
echo -e "  • Your Spring Boot app should be running on localhost:8091"

echo ""
echo -e "${YELLOW}🔧 Starting services...${NC}"

# Start the services
$DOCKER_COMPOSE -f remote-docker-compose.yml up -d

if [ $? -eq 0 ]; then
    echo ""
    echo -e "${GREEN}✅ Services started successfully!${NC}"
    echo ""
    echo -e "${BLUE}🌐 Access URLs:${NC}"
    echo -e "  • Main application: ${GREEN}http://localhost${NC} (proxied through Nginx)"
    echo -e "  • Direct Spring Boot: ${GREEN}http://localhost:8091${NC}"
    echo -e "  • Swagger UI: ${GREEN}http://localhost/swagger-ui.html${NC}"
    echo -e "  • API Docs: ${GREEN}http://localhost/api-docs${NC}"
    echo -e "  • EMQX Dashboard: ${GREEN}http://localhost:18083${NC} (admin/d1p0f8123)"
    echo -e "  • Nginx Status: ${GREEN}http://localhost/nginx_status${NC}"
    echo ""
    echo -e "${YELLOW}📝 Note: Make sure your Spring Boot application is running on port 8091${NC}"
    echo -e "${YELLOW}📝 To stop services: ${BLUE}$DOCKER_COMPOSE -f remote-docker-compose.yml down${NC}"
    echo ""
    echo -e "${BLUE}📊 Service Status:${NC}"
    $DOCKER_COMPOSE -f remote-docker-compose.yml ps
else
    echo -e "${RED}❌ Failed to start services. Check the logs above.${NC}"
    exit 1
fi 