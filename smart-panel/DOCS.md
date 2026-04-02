# FastyBird Smart Panel - Home Assistant Add-on

## Overview

This add-on installs and runs [FastyBird Smart Panel](https://smart-panel.fastybird.com) within your Home Assistant instance. Smart Panel provides a customizable display interface for visualizing and controlling smart home devices from any touchscreen or browser.

## Features

- **Real-time dashboard** with device control widgets
- **Admin interface** for managing devices, dashboards, and settings
- **Multi-protocol support** — integrates with Home Assistant, Shelly (v1 & NG), Zigbee2MQTT, WLED, and more
- **Touchscreen optimized** for Raspberry Pi and embedded displays
- **HA sidebar integration** — accessible directly from the Home Assistant sidebar via ingress
- **Direct access** — also available at `http://<your-ha-ip>:3000`

## How It Works

The add-on runs the Smart Panel backend (NestJS) which:

1. Provides the REST API for device management and control
2. Serves the admin web interface for configuration
3. Handles WebSocket connections for real-time updates
4. Stores data in a local SQLite database
5. Runs database migrations automatically on startup

An nginx reverse proxy handles HA ingress path rewriting so the panel works seamlessly in the HA sidebar.

## Accessing the Panel

After starting the add-on:

1. **HA Sidebar** — Click "Smart Panel" in the Home Assistant sidebar
2. **Direct URL** — Open `http://<your-ha-ip>:3000` in any browser
3. **Web UI button** — Click "Open Web UI" on the add-on details page

On first launch you'll be guided through the onboarding process.

## Configuration

Configuration is minimal — most settings are managed through the Smart Panel admin interface itself.

### Option: `log_level`

Controls the verbosity of application logs. Valid values: `debug`, `info`, `warn`, `error`.

### Option: `token_secret`

A secret key used for authentication token encryption. If left empty, a random secret is generated on each startup. Set a fixed value to persist login sessions across restarts.

## Home Assistant Integration

The add-on includes a built-in Home Assistant device plugin that can discover and control your HA entities. Configure the HA connection (API token and hostname) through the Smart Panel admin interface under **Configuration > Plugins > Home Assistant**.

To create a long-lived access token: go to your HA **User Profile > Long-Lived Access Tokens > Create Token**.

## Device Discovery

The add-on runs with **host networking** enabled, allowing it to discover devices on your local network via:

- **CoAP** — Shelly v1 devices (UDP multicast)
- **mDNS** — Shelly NG, WLED, and other devices
- **MQTT** — Zigbee2MQTT (configure broker address in Smart Panel settings)

## Data Storage

All persistent data is stored in the add-on's `/data` directory, which survives restarts and updates:

- `/data/db/` — SQLite database
- `/data/config/` — Application configuration (generated on first start)

To reset the configuration, stop the add-on, delete `/data/config/config.yaml` via SSH, and restart.

## Troubleshooting

### Add-on fails to start

Check the add-on logs for error details. Common issues:

- **Port conflict** — Another service may be using port 3000. Check the add-on log for bind errors.
- **Migration errors** — Usually resolve on retry. If persistent, delete `/data/db/` and restart for a fresh database.

### Panel shows blank page

- Clear your browser cache
- Check the browser developer console (F12) for errors
- Verify the add-on is running in the add-on details page

### Home Assistant plugin can't connect

- Ensure you've created a long-lived access token in HA
- Verify the hostname is correct (e.g., `192.168.1.100:8123`)
- Check that the token hasn't expired

## Support

- [Documentation](https://smart-panel.fastybird.com/docs)
- [GitHub Issues](https://github.com/fastybird/smart-panel-ha/issues)
