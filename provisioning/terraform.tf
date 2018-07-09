# DigitalOcean authentication token
variable "do_token" {}

variable "gcp_project_id" {
  default = "busun-158105"
}

provider "digitalocean" {
  token   = "${var.do_token}"
  version = "~> 0.1"
}

provider "google" {
  credentials = "${file("gcp_credentials.json")}"
  project     = "${var.gcp_project_id}"
  region      = "us-east1"
  version     = "~> 1.14"
}

# Install from https://github.com/coreos/terraform-provider-ct
provider "ct" {}

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
