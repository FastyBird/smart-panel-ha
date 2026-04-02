ARG BUILD_FROM
ARG SMART_PANEL_VERSION=latest

# ---------------------------------------------------------------------------
# Stage 1 – grab the pre-built app from the upstream multi-arch image
# (native amd64 + arm64 builds, no QEMU compilation needed)
# ---------------------------------------------------------------------------
FROM ghcr.io/fastybird/smart-panel:${SMART_PANEL_VERSION} AS upstream

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

# Application (pre-compiled with native modules for the correct arch)
COPY --from=upstream /app /app

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
    io.hass.version="0.7.0-alpha"
