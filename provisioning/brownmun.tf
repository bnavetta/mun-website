# Can set this from the command line or a tfvars file
variable "do_token" {}

provider "digitalocean" {
    token = "${var.do_token}"
}

resource "digitalocean_droplet" "brownmun" {
    name = "brownmun"
    image = "fedora-27-x64"
    region = "nyc1"
    size = "s-2vcpu-2gb"

    monitoring = true
    ipv6 = true
    private_networking = true

    ssh_keys = [18668469]
}

resource "digitalocean_floating_ip" "brownmun-public" {
    droplet_id = "${digitalocean_droplet.brownmun.id}"
    region = "${digitalocean_droplet.brownmun.region}"
}

resource "digitalocean_firewall" "web" {
    name = "web-firewall"

    droplet_ids = ["${digitalocean_droplet.brownmun.id}"]

    inbound_rule = [
        # Allow SSH
        {
            protocol = "tcp", port_range = "22", source_addresses = ["0.0.0.0/0", "::/0"]
        },
        # Allow HTTP and HTTPS
        {
            protocol = "tcp", port_range = "80", source_addresses = ["0.0.0.0/0", "::/0"]
        },
        {
            protocol = "tcp", port_range = "443", source_addresses = ["0.0.0.0/0", "::/0"]
        },
        # Allow PostgreSQL
        # TODO: is it better to just tunnel through SSH?
        {
            protocol = "tcp", port_range = "5432", source_addresses = ["0.0.0.0/0", "::0"]
        },
        # Allow Kubernetes
        {
            protocol = "tcp", port_range = "6443", source_addresses = ["0.0.0.0/0", "::0"]
        }
    ]

    outbound_rule = [
        # Allow DNS lookups
        { protocol = "tcp", port_range = "53", destination_addresses   = ["0.0.0.0/0", "::/0"] },
        { protocol = "udp", port_range = "53", destination_addresses   = ["0.0.0.0/0", "::/0"] },

        # Allow making HTTP / HTTPS requests
        { protocol = "tcp", port_range = "80", destination_addresses   = ["0.0.0.0/0", "::/0"] },
        { protocol = "tcp", port_range = "443", destination_addresses   = ["0.0.0.0/0", "::/0"] },
    ]
}