# FastyBird Smart Panel - Home Assistant Add-on

A Home Assistant add-on that installs and runs [FastyBird Smart Panel](https://github.com/FastyBird/smart-panel) — a customizable display interface for visualizing and controlling smart homes from any touchscreen.

## About

FastyBird Smart Panel provides:

- **Real-time dashboard** for controlling smart home devices
- **Admin interface** for configuration and management
- **Multi-protocol support** — Home Assistant, Shelly, Zigbee2MQTT, WLED, and more
- **Touchscreen optimized** UI for embedded displays

## Installation

1. Open your Home Assistant instance
2. Navigate to **Settings** > **Add-ons** > **Add-on Store**
3. Click the three-dot menu in the top right and select **Repositories**
4. Add this repository URL: `https://github.com/fastybird/smart-panel-ha`
5. Find **Smart Panel** in the add-on store and click **Install** (the Supervisor pulls the prebuilt image from GitHub Container Registry; ensure the package is **public**)
6. Start the add-on and access it from the sidebar

## Configuration

```yaml
log_level: info
token_secret: ""
```

| Option | Description | Default |
|---|---|---|
| `log_level` | Application log level (`debug`, `info`, `warn`, `error`) | `info` |
| `token_secret` | Secret for auth token encryption. Leave empty to auto-generate each start; set a fixed value to keep sessions across restarts | *(empty)* |

**Image:** The add-on uses **`ghcr.io/fastybird/smart-panel`** with the tag in `version` (for example `0.4.0-alpha`), matching `docker pull ghcr.io/fastybird/smart-panel:0.4.0-alpha`. To **build on the HA host** instead (development or private registry), remove the `image` line from `smart-panel/config.yaml` and align `version` with your workflow.

## Support

- [Smart Panel Documentation](https://github.com/FastyBird/smart-panel)
- [Issue Tracker](https://github.com/fastybird/smart-panel-ha/issues)

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.
