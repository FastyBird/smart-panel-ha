# Smart Panel HA Add-on — Project Notes

## Architecture

- **HA add-on** that installs FastyBird Smart Panel from the `@fastybird/smart-panel` npm package
- **Multi-stage Docker build**: Stage 1 (node:24-alpine) installs npm deps, Stage 2 (hassio-addons/base) copies app + Node.js + rootfs overlay
- **s6-overlay v3** manages services: smart-panel (backend) + nginx (ingress proxy) + init-nginx (config render)
- **Backend** on `0.0.0.0:3000` (port exposed to host), **nginx** on dynamic ingress port (assigned by HA)
- **Host networking** enabled for CoAP/mDNS multicast (Shelly, WLED discovery)

## Key Paths (npm layout)

- Entry point: `/app/node_modules/@fastybird/smart-panel-backend/dist/main.js`
- Admin UI: `/app/node_modules/@fastybird/smart-panel-admin/dist`
- TypeORM CLI: `/app/node_modules/typeorm/cli.js` (hoisted as direct dependency)
- DataSource: `/app/node_modules/@fastybird/smart-panel-backend/dist/dataSource.js`
- Config: `/data/config/config.yaml` (generated on first start only)
- Database: `/data/db/`

## HA Integration

- `SUPERVISOR_TOKEN` env var — auto-injected by HA, used for Supervisor API auth
- `PLATFORM_TYPE=home-assistant` env var — tells backend the platform type
- Supervisor API: `http://supervisor/core/api/...` with Bearer token
- Supervisor WebSocket: `ws://supervisor/core/websocket`
- Config flags: `homeassistant_api: true`, `hassio_api: true`, `auth_api: true`

## CI/CD

- GitHub Actions builds on push to main + tags + manual dispatch
- Image tags: `ghcr.io/fastybird/smart-panel-{arch}:{version}` (lowercased owner)
- `SMART_PANEL_VERSION` build arg read from `build.yaml`
- Clear GHA cache before version bumps to avoid stale npm packages

## Version Bump Checklist

1. Check npm: `curl -s "https://registry.npmjs.org/@fastybird/smart-panel" | python3 -c "..."`
2. Update `config.yaml` version
3. Update `build.yaml` SMART_PANEL_VERSION
4. Update `Dockerfile` label io.hass.version
5. Update `CHANGELOG.md` (use `## x.y.z` format, no brackets/dates — HA parses it)
6. Clear GHA cache: `gh cache list | awk '{print $1}' | while read id; do gh cache delete "$id"; done`
7. Commit and push

## Conventions

- CHANGELOG headings: `## x.y.z-alpha` (no brackets, no dates) — HA Supervisor parses these
- Architectures: amd64, aarch64 only (Node.js 24 dropped armv7)
- Config generated only on first start — won't overwrite existing user config
- HA persists `/data` even after add-on uninstall
