# Changelog

## [1.0.1] - 2026-04-01

### Fixed

- Remove default `image` from `config.yaml` so the Supervisor **builds locally**; anonymous pulls from GHCR failed with **403 denied** when packages were private or unpublished

## [1.0.0] - 2026-04-01

### Changed

- Build from published **`@fastybird/smart-panel`** on npm (bundles backend + admin) instead of cloning and building the monorepo with pnpm
- Runtime uses **Node.js 24** (copied from the official image; native addons still compile in the builder stage)

### Fixed

- Register s6-overlay services in the `user` bundle (`contents.d`) so `init-nginx`, Smart Panel, and `nginx` actually start
- Set `init: false` for s6-overlay v3 (required; `init: true` breaks the container init)
- Ensure `/etc/nginx/servers` exists before rendering ingress config
- Add `nginx` service dependencies on `init-nginx` and `smart-panel`

### Added

- `icon.png` for the add-on store

## [dev] - Unreleased

### Added

- Initial Home Assistant add-on for FastyBird Smart Panel
- NestJS backend with SQLite database
- Vue.js admin interface served as static files
- Home Assistant Ingress support for sidebar integration
- Multi-architecture support (amd64, aarch64, armv7)
- Configurable log level and token secret
- Persistent data storage in `/data` directory
