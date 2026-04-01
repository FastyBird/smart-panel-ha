ARG BUILD_FROM
# =============================================================================
# Stage 1: Install published @fastybird/smart-panel (backend + admin) from npm
# =============================================================================
FROM node:24-alpine AS builder

ARG SMART_PANEL_VERSION=latest

# sqlite3, bcrypt, etc. ship native addons — need a toolchain on Alpine
RUN apk add --no-cache \
    python3 \
    make \
    g++ \
    sqlite-dev

WORKDIR /app

# Pin typeorm at the app root so `node ./node_modules/typeorm/cli.js` is available
# for manual migration commands. Migrations auto-run at startup via NestJS
# (FB_DB_MIGRATIONS_RUN=true), but having the CLI accessible is useful for
# troubleshooting. Without the pin, npm may nest typeorm under the backend only.
RUN echo "{\"name\":\"smart-panel-ha-runtime\",\"private\":true,\"dependencies\":{\"@fastybird/smart-panel\":\"${SMART_PANEL_VERSION}\",\"typeorm\":\"^0.3.28\"}}" > package.json \
    && npm install --omit=dev

# =============================================================================
# Stage 2: Production image using Home Assistant community base
# =============================================================================
FROM ${BUILD_FROM}

# Node.js 24 (same major as upstream packages); avoids older apk node on the base image
COPY --from=node:24-alpine /usr/local /usr/local
ENV PATH="/usr/local/bin:${PATH}"

RUN apk add --no-cache \
    nginx \
    sqlite

WORKDIR /app

COPY --from=builder /app /app

# Create persistent data directories and nginx include dir for ingress config
RUN mkdir -p /data/db /data/config /etc/nginx/servers

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
    io.hass.version="0.4.0-alpha"
