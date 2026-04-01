# Changelog

## [0.7.0-alpha] - 2026-04-02

### Changed

- Bump to `@fastybird/smart-panel@0.7.0-alpha.0` (includes migration fixes)

### Fixed

- Restore multi-stage Docker build from npm packages instead of pulling a pre-built image that was missing runtime dependencies (`class-validator`, etc.)
- Fix GitHub Actions: lowercase Docker image tags (`FastyBird` → `fastybird`) to satisfy Docker registry naming rules
- Drop armv7 architecture (Node.js 24 removed 32-bit ARM support)
- Move backend to port 3001 to avoid bind conflict with nginx ingress on port 3000
- Rewrite absolute asset/API paths via nginx `sub_filter` for HA ingress compatibility

## [0.4.0-alpha.1] - 2026-04-01

### Fixed

- Add **`typeorm`** as a direct npm dependency in the Docker build so `/app/node_modules/typeorm/cli.js` exists. Migration scripts in `@fastybird/smart-panel` call that path; without a hoisted `typeorm` package, Node reported **Cannot find module '.../typeorm/cli.js'**

## [0.4.0-alpha] - 2026-04-01

### Changed

- Point `image` at **`ghcr.io/fastybird/smart-panel`** with tag **`0.4.0-alpha`** (multi-arch manifest; same reference as `docker pull ghcr.io/fastybird/smart-panel:0.4.0-alpha`)

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
