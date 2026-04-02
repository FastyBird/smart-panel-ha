# Changelog

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
