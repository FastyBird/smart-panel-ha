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
# Stage 2: Production image
# =============================================================================
FROM ${BUILD_FROM}

# Install runtime dependencies
RUN apk add --no-cache \
    nodejs \
    npm \
    sqlite \
    bash \
    jq \
    curl

WORKDIR /app

# Copy built backend
COPY --from=builder /build/apps/backend/dist ./dist
COPY --from=builder /build/apps/backend/node_modules ./node_modules
COPY --from=builder /build/apps/backend/package.json ./package.json

# Copy built admin UI to be served as static files
COPY --from=builder /build/apps/admin/dist ./public

# Create data directories
RUN mkdir -p /data/db /data/config

# Copy entrypoint
COPY run.sh /run.sh
RUN chmod a+x /run.sh

# Labels for Home Assistant
LABEL \
    io.hass.name="Smart Panel" \
    io.hass.description="A customizable display interface for visualizing and controlling smart homes" \
    io.hass.type="addon" \
    io.hass.version="dev"

EXPOSE 3000

CMD [ "/run.sh" ]
