# Floating IPs are stable public IP addresses reserved from DO. The address can be bound to different
# droplets without having to change any DNS settings.
resource "digitalocean_floating_ip" "public_ip" {
  droplet_id = "${digitalocean_droplet.brownmun.id}"
  region     = "${digitalocean_droplet.brownmun.region}"
}

output "public_ip" {
  value = "${digitalocean_floating_ip.public_ip.ip_address}"
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

  # Allow outbound DNS, NTP, HTTP, and HTTPS
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
      protocol              = "udp"
      port_range            = "123"
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
