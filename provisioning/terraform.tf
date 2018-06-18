# DigitalOcean authentication token
variable "do_token" {}

provider "digitalocean" {
  token   = "${var.do_token}"
  version = "~> 0.1"
}

# Install from https://github.com/coreos/terraform-provider-ct
provider "ct" {}

resource "digitalocean_ssh_key" "tech_key" {
  name       = "Brown MUN Key"
  public_key = "${file("keys/id_rsa.pub")}"
}

# CoreOS uses a provisioning system called Ignition (https://coreos.com/ignition/docs/latest/)
# Ignition files are not-particularly-readable JSON, which is usally generated from Container Linux Config YAML
# (https://coreos.com/os/docs/latest/provisioning.html). CoreOS has a Terraform provider to do this transformation,
# so we can pass it into DigitalOcean when we create the droplet (VM).

data "ct_config" "brownmun" {
  content      = "${file("cl-config.yaml")}"
  platform     = "digitalocean"
  pretty_print = true
}

resource "digitalocean_droplet" "brownmun" {
  image              = "coreos-stable"
  name               = "brownmun"
  size               = "s-2vcpu-2gb"                           # 2 vCPUs, 2 GB of memory, 60 GB of disk for $15/month
  region             = "nyc3"
  ipv6               = true
  private_networking = true
  ssh_keys           = ["${digitalocean_ssh_key.tech_key.id}"]
  user_data          = "${data.ct_config.brownmun.rendered}"
}

resource "digitalocean_floating_ip" "public_ip" {
  droplet_id = "${digitalocean_droplet.brownmun.id}"
  region     = "${digitalocean_droplet.brownmun.region}"
}

resource "digitalocean_firewall" "public" {
  name = "brownmun-public"

  droplet_ids = ["${digitalocean_droplet.brownmun.id}"]

  # Allow inbound SSH, HTTP, and HTTPS
  inbound_rule = [
    {
      protocol         = "tcp"
      port_range       = "22"
      source_addresses = ["0.0.0.0/0", "::/0"]
    },
    {
      protocol         = "tcp"
      port_range       = "80"
      source_addresses = ["0.0.0.0/0", "::/0"]
    },
    {
      protocol         = "tcp"
      port_range       = "443"
      source_addresses = ["0.0.0.0/0", "::/0"]
    },
  ]

  # Allow outbound DNS, HTTP, and HTTPS
  outbound_rule = [
    {
      protocol              = "tcp"
      port_range            = "53"
      destination_addresses = ["0.0.0.0/0", "::/0"]
    },
    {
      protocol              = "udp"
      port_range            = "53"
      destination_addresses = ["0.0.0.0/0", "::/0"]
    },
    {
      protocol              = "tcp"
      port_range            = "80"
      destination_addresses = ["0.0.0.0/0", "::/0"]
    },
    {
      protocol              = "tcp"
      port_range            = "443"
      destination_addresses = ["0.0.0.0/0", "::/0"]
    },
  ]
}
