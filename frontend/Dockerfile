# Build stage
FROM node:18 AS builder
WORKDIR /app
COPY package*.json ./
COPY . .
RUN npm install
RUN npm run build

# Production stage
FROM nginx:alpine

# Copy SSL certificates (must exist in your project's ./ssl directory)
COPY ./ssl/StaffGuard.crt /etc/nginx/ssl/
COPY ./ssl/staffguard.key /etc/nginx/ssl/

# Copy built assets from builder stage
COPY --from=builder /app/build /usr/share/nginx/html

# Copy custom nginx configuration
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expose both HTTP (80) and HTTPS (443) ports
EXPOSE 80 443

# Optional: Add health check
HEALTHCHECK --interval=30s --timeout=3s \
  CMD wget --no-verbose --tries=1 --spider http://localhost:80/ || exit 1