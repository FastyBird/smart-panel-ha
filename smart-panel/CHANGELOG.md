# Changelog

## [0.2.0-alpha] - 2026-04-03

### Added

- InfluxDB v2 storage plugin with full read/write support and admin UI configuration
- Dynamic storage plugin selector in admin UI (disabled plugins shown grayed out)
- Auto-detect Home Assistant add-on platform via `SUPERVISOR_TOKEN` environment variable
- Raspberry Pi throttle status reporting when running as HA add-on on RPi hardware
- `PLATFORM_TYPE=home-assistant` environment variable for platform detection

### Fixed

- HA WebSocket connection reliability — ping/pong keepalive, reconnect guards, correct WS path for supervisor mode
- Shelly NG (Gen 2) — health check improvements, crash fix in message handler, better reconnect backoff
- Shelly V1 (Gen 1) — HTTP ping fallback when CoAP multicast is lost, device listener cleanup
- Platform API — fix throttle status return type, add 30s timeout to Supervisor API calls

## [0.1.0-alpha] - 2026-04-02

### Added

- Home Assistant add-on for FastyBird Smart Panel
- Multi-stage Docker build from `@fastybird/smart-panel` npm package
- NestJS backend with SQLite database and automatic migrations
- Vue.js admin interface with HA ingress support (relative asset paths)
- HA sidebar integration via ingress with nginx reverse proxy
- Direct web access on port 3000
- Host networking for CoAP/mDNS multicast device discovery (Shelly, WLED)
- Home Assistant device plugin (auto-enabled, configure via Smart Panel UI)
- Multi-architecture support (amd64, aarch64)
- s6-overlay v3 service management
- Configurable log level and token secret
- Persistent data storage in `/data` directory
- GitHub Actions CI/CD pipeline for automated image builds
