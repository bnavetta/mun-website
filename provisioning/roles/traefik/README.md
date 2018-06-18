# traefik

This role configures the [traefik](https://traefik.io/) reverse proxy as a systemd-managed Docker container.
It uses traefik's [Docker provider](https://docs.traefik.io/configuration/backends/docker/) so that containers
with the correct labels will automatically be exposed.

Traefik is configured to expose its monitoring dashboard at [traefik.busun.org](https://traefik.busun.org). You
can log in with the username `admin` and the usual password.