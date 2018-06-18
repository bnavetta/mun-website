# Provisioning

This directory contains configuration to the the BUSUN and BUCS website running live. Our cloud provider is
[DigitalOcean](https://www.digitalocean.com/), which we use because they're simple and affordable. It also helps
that they have a cute shark mascot :)

We run [CoreOS Container Linux](https://coreos.com/os/docs/latest/), which is a minimal Linux distribution designed
for running containers, especially with [Docker](https://www.docker.com/). This means that it's fast to boot up,
easy to keep up-to-date, and pretty simple to maintain. Container Linux is mainly designed to be run in large 
clusters, so we don't use all of its features, or all that many of CoreOS's other projects (like Tectonic for
running Kubernetes). Instead, we use [systemd](https://www.freedesktop.org/wiki/Software/systemd/) directly to keep
containers running. In the past, CoreOS had a remote service for managing systemd units called fleet, but that's been
deprecated in favor of Kubernetes. Running Docker containers with systemd can be a bit wonky, but works well enough
(there's some overlap in what they do, so you have to decide which does what).

The bulk of this is set up by [Terraform](https://www.terraform.io/). Terraform is a tool for describing 
infrastructure in declarative configuration files. This lets us write everything down precisely and then let
Terraform make whatever changes are needed. It configures our DigitalOcean server, firewalls, and public IP address. 

Since our domain names are registered [through Google](https://domains.google/#/), we use them to configure DNS
records as well. In theory, we could set up DNS records with DigitalOcean and manage them from Terraform, but there's
not a particularly pressing reason to change.

Since we have some custom images, they're stored on [Google Container Registry](https://cloud.google.com/container-registry/).
GCR only charges for the associated storage and network costs, so it's dirt-cheap (less than $10 per year to store
20GB).

The overall site architecture is broken up into a couple containers:

* [Traefik](https://traefik.io/) is a pretty neat reverse proxy written in Go. A reverse proxy is a server that sits
  between the wider internet and all your other sites. It routes between them so that you can serve different domains
  from the same machine. Generally, as in our case, the reverse proxy also handles common concerns like TLS and
  authentication. One of the nice things about Traefik is that it has built-in support for
  [Let's Encrypt](https://letsencrypt.org/), making it really easy to keep our certificates up-to-date.
* [PostgreSQL](https://www.postgresql.org/), commonly referred to as Postgres, is an awesome open-source relational
  database. It holds all of our data for the BUSUN and BUCS websites, like school information, committees, positions,
  and attendance records. We use the official Postgres Docker image with a custom init script that creates the separate BUSUN and BUCS databases.
* The BUSUN and BUCS websites are two Java applications built on a shared base. We build each into separate Docker
  images and run them behind Traefik. Deploying an update involves pushing a new image to GCR and configuring the
  systemd unit to use the new version.
* Static assets (CSS and JavaScript) are served separately from the Java webapps using
  [NGINX](https://www.nginx.com/). Since NGINX is a dedicated web server, it can be smarter about compressing, 
  caching, and file serving than a Java application server. The BUSUN and BUCS sites use the same static assets,
  with [CSS variables](https://developer.mozilla.org/en-US/docs/Web/CSS/Using_CSS_variables) to style the sites
  differently.

Our containers are largely configured through [Ansible](https://www.ansible.com/). Like Terraform lets us 
declaratively manage infrastructure, Ansible is used to declaratively manage how that infrastructure is set up. Since
we're using Container Linux, that mostly means copying over configuration files and systemd service definitions.

We have a few different Ansible playbooks:

* `bootstrap.yml` does the initial configuration of a new system, like installing Python
* `site.yml` is the main configuration playbook. It sets up all of our systemd units and configures Traefik
  and PostgreSQL
* `deploy.yml` reconfigures the website containers to use a new version. It's separate playbook so that we don't
  have to run through the entire base configuration on every deploy. The `mun` deployment commands call this after
  pushing new images with Gradle.