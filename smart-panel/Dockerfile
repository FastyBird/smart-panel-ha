ARG BUILD_FROM
# =============================================================================
# Stage 1: Build the smart-panel backend and admin from source
# =============================================================================
FROM node:20-alpine AS builder

ARG SMART_PANEL_VERSION=main

RUN apk add --no-cache \
    git \
    python3 \
    make \
    g++ \
    sqlite-dev

WORKDIR /build

# Clone the smart-panel repository
RUN git clone --depth 1 --branch "${SMART_PANEL_VERSION}" \
    https://github.com/FastyBird/smart-panel.git .

# Enable and activate pnpm
RUN corepack enable && corepack prepare pnpm@latest --activate

# Install all dependencies
RUN pnpm install --frozen-lockfile

# Build the backend
RUN pnpm --filter @fastybird/smart-panel-backend build

# Build the admin interface
RUN pnpm --filter @fastybird/smart-panel-admin build

# Prune dev dependencies for backend production install
WORKDIR /build/apps/backend
RUN pnpm install --prod --ignore-scripts

# =============================================================================
# Stage 2: Production image using Home Assistant community base
# =============================================================================
FROM ${BUILD_FROM}

# Install runtime dependencies
RUN apk add --no-cache \
    nodejs \
    npm \
    nginx \
    sqlite

WORKDIR /app

# Copy built backend
COPY --from=builder /build/apps/backend/dist ./dist
COPY --from=builder /build/apps/backend/node_modules ./node_modules
COPY --from=builder /build/apps/backend/package.json ./package.json

# Copy built admin UI to be served by the backend
COPY --from=builder /build/apps/admin/dist ./public

# Create persistent data directories
RUN mkdir -p /data/db /data/config

# Copy rootfs overlay (s6-overlay services, nginx config)
COPY rootfs /

# Ensure all service scripts are executable
RUN chmod a+x \
    /etc/s6-overlay/s6-rc.d/init-nginx/run \
    /etc/s6-overlay/s6-rc.d/smart-panel/run \
    /etc/s6-overlay/s6-rc.d/smart-panel/finish \
    /etc/s6-overlay/s6-rc.d/nginx/run

# Labels for Home Assistant
LABEL \
    io.hass.name="Smart Panel" \
    io.hass.description="A customizable display interface for visualizing and controlling smart homes" \
    io.hass.type="addon" \
    io.hass.version="dev"
