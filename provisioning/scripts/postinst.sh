#!/bin/bash

set -eou pipefail

# Post-install script for our Kubernetes cluster
# This does a couple things that are difficult to do in Ansible but are prerequisites for our
# Helm setup.

# To run this, you may need the Kubernetes admin config file, located at /etc/kubernetes/admin.conf on the VM. Download
# it and run this script with the KUBECONFIG environment variable set to the location you downloaded it to.

# external_ip=$(terraform state show digitalocean_floating_ip.brownmun-public | grep ip_address | awk '{print $3}')
# Since we're using DigitalOcean floating IPs, we have to bind the ingress controller to the anchor IP, which is a special
# IP address specifically for floating IPs that's bound to the same network interface as the droplet's public IP address
# AFAIK, the only programmatic way to get at this is to access the metadata service from the droplet itself.
external_ip=$(ssh root@kube.busun.org curl -s http://169.254.169.254/metadata/v1/interfaces/public/0/anchor_ipv4/address)

# Create an admin service account for tiller
kubectl create serviceaccount --namespace=kube-system tiller
kubectl create clusterrolebinding tiller-admin-binding --clusterrole=cluster-admin --serviceaccount=kube-system:tiller

helm init \
    --override 'spec.template.spec.containers[0].command'='{/tiller,--storage=secret}' \
    --service-account=tiller

# Wait for Helm to set up
sleep 10

# Install the NGINX ingress controller
# This basically lets us run a NGINX reverse proxy entirely managed by Kubernetes
# Because the default NodePort range for Kubernetes is 30000+, we bind to ports in that range and use iptables rules
# (in the Ansible config) to forward ports 80 and 443 to that. We don't want to let Kubernetes services bind to low ports
# by default, because that could clobber host services (for example, Kubernetes might accidentally assign some service to
# bind to port 22).

helm install --name nginx-ingress stable/nginx-ingress \
    --set controller.service.type=NodePort \
    --set "controller.service.externalIPs={$external_ip}" \
    --set controller.service.nodePorts.http=30080 \
    --set controller.service.nodePorts.https=30443 \
    --set rbac.create=true

# And this lets us automatically provision Let's Encrypt TLS certificates
# It's a bit more generic than Let's Encrypt, but the brownmun Helm chart adds resources for Lets' Encrypt specifically
helm install --name cert-manager stable/cert-manager