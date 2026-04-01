# FastyBird Smart Panel - Home Assistant Add-on

## Overview

This add-on installs and runs [FastyBird Smart Panel](https://github.com/FastyBird/smart-panel) within your Home Assistant instance. Smart Panel provides a customizable display interface for visualizing and controlling smart home devices from any touchscreen or browser.

## Features

- **Real-time dashboard** with device control widgets
- **Admin interface** for managing devices, dashboards, and settings
- **Multi-protocol support** — integrates with Home Assistant, Shelly, Zigbee2MQTT, WLED, and more
- **Touchscreen optimized** for Raspberry Pi and embedded displays
- **Ingress support** — accessible directly from the Home Assistant sidebar

## How It Works

The add-on runs the Smart Panel backend (NestJS) server which:

1. Provides the REST API for device management and control
2. Serves the admin web interface for configuration
3. Handles WebSocket connections for real-time updates
4. Stores data in a local SQLite database

The Docker image installs the published npm package **`@fastybird/smart-panel`**, which pulls in the backend and admin UI as dependencies. The runtime uses **Node.js 24** (aligned with upstream). To pin a specific release when building yourself, set the Docker build argument `SMART_PANEL_VERSION` (for example `0.1.0-alpha.5` or `latest`) in `build.yaml` or when invoking `docker build`.

To install from **GitHub Packages** instead of the public npm registry, add an `.npmrc` in the build context (for example `//npm.pkg.github.com/:_authToken=${NPM_TOKEN}` and scope registry) and pass a secret at build time; the default Dockerfile uses the npmjs registry only.

## Configuration

### Option: `log_level`

Controls the verbosity of application logs. Valid values: `debug`, `info`, `warn`, `error`.

### Option: `token_secret`

A secret key used for authentication token encryption. If left empty, a random secret is generated on each startup. Set a fixed value to persist sessions across restarts.

## Data Storage

All persistent data is stored in the add-on's `/data` directory, which survives add-on restarts and updates:

- `/data/db/` — SQLite database files
- `/data/config/` — Application configuration

## Prebuilt images

The add-on is distributed as container images on GitHub Container Registry. After you add this repository in the Add-on Store, Home Assistant pulls the image matching your architecture and the version in `config.yaml`.

If the package is not yet public, open the **Packages** section of this GitHub organization or user, select the `smart-panel-*` image, and set its visibility to **public** so Home Assistant can pull it.

## Building locally (developers)

To build the image on your Home Assistant host instead of pulling from GHCR, remove the `image` line from the add-on `config.yaml` and update the repository in the store so the Supervisor rebuilds from the `Dockerfile`.

## Accessing the Panel

After starting the add-on, access Smart Panel via:

1. **Sidebar** — Click "Smart Panel" in the Home Assistant sidebar (ingress)
2. **Direct URL** — If you've configured a port mapping, access it at `http://<your-ha-ip>:<port>`

## Troubleshooting

### Add-on fails to start

Check the add-on logs in the Home Assistant UI for error details. Common issues:

- **Port conflict** — Another service may be using port 3000
- **Memory** — The backend requires adequate memory; ensure your device has sufficient resources

### Panel shows blank page

- Clear your browser cache
- Check that the add-on is running and healthy in the add-on details page
- Review the logs for any backend errors

## Support

- [GitHub Issues](https://github.com/fastybird/smart-panel-ha/issues)
- [Smart Panel Documentation](https://github.com/FastyBird/smart-panel)
