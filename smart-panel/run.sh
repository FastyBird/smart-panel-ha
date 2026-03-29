#!/usr/bin/env bash
set -e

# =============================================================================
# FastyBird Smart Panel - Home Assistant Add-on Entrypoint
# =============================================================================

CONFIG_FILE="/data/options.json"

# Read configuration from Home Assistant add-on options
if [ -f "${CONFIG_FILE}" ]; then
    LOG_LEVEL=$(jq -r '.log_level // "info"' "${CONFIG_FILE}")
    TOKEN_SECRET=$(jq -r '.token_secret // ""' "${CONFIG_FILE}")
else
    LOG_LEVEL="info"
    TOKEN_SECRET=""
fi

# Generate a random token secret if not provided
if [ -z "${TOKEN_SECRET}" ]; then
    TOKEN_SECRET=$(head -c 32 /dev/urandom | base64 | tr -d '/+=' | head -c 32)
    echo "Generated random token secret"
fi

# Export environment variables for the smart-panel backend
export NODE_ENV="production"
export FB_BACKEND_PORT="3000"
export FB_APP_HOST="0.0.0.0"
export FB_CONFIG_PATH="/data/config"
export FB_DB_PATH="/data/db"
export FB_ADMIN_UI_PATH="/app/public"
export FB_TOKEN_SECRET="${TOKEN_SECRET}"
export FB_LOG_LEVEL="${LOG_LEVEL}"

echo "========================================"
echo " FastyBird Smart Panel"
echo " Log level: ${LOG_LEVEL}"
echo " Backend port: 3000"
echo " Data directory: /data"
echo "========================================"

# Start the NestJS backend
exec node /app/dist/main.js
