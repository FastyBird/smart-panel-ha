ARG BUILD_FROM
ARG SMART_PANEL_VERSION=latest

# ---------------------------------------------------------------------------
# Stage 1 – install @fastybird/smart-panel from npm with production deps
# ---------------------------------------------------------------------------
FROM node:24-alpine AS builder

ARG SMART_PANEL_VERSION

# Native-addon build tools (sqlite3, bcrypt) and git (github: deps)
RUN apk add --no-cache python3 make g++ sqlite-dev git

WORKDIR /app

# Create a minimal package.json that pulls the smart-panel package.
# Hoist typeorm so /app/node_modules/typeorm/cli.js is reachable by
# migration helper scripts that hard-code that path.
RUN printf '{"name":"smart-panel-ha-runtime","private":true,"dependencies":{"@fastybird/smart-panel":"%s","typeorm":"^0.3.28"}}' \
      "${SMART_PANEL_VERSION}" > package.json \
 && npm install --omit=dev

# ---------------------------------------------------------------------------
# Stage 2 – production image on the Home Assistant base
# ---------------------------------------------------------------------------
FROM ${BUILD_FROM}

# Copy Node.js runtime from the official image
COPY --from=node:24-alpine /usr/local/bin/node /usr/local/bin/node
COPY --from=node:24-alpine /usr/local/lib/node_modules /usr/local/lib/node_modules
RUN ln -sf /usr/local/lib/node_modules/npm/bin/npm-cli.js /usr/local/bin/npm

# Runtime dependencies
RUN apk add --no-cache nginx sqlite sqlite-libs libstdc++

# Application
COPY --from=builder /app /app

# Ensure directories exist
RUN mkdir -p /data/db /data/config /etc/nginx/servers

# Overlay: s6-overlay services, nginx configs
COPY rootfs /

# Make service scripts executable
RUN chmod a+x /etc/s6-overlay/s6-rc.d/*/run 2>/dev/null || true \
 && chmod a+x /etc/s6-overlay/s6-rc.d/*/finish 2>/dev/null || true

LABEL \
    io.hass.name="Smart Panel" \
    io.hass.description="A customizable display interface for visualizing and controlling smart homes" \
    io.hass.type="addon" \
    io.hass.version="0.5.0-alpha"
