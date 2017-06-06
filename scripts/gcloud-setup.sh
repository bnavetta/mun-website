#!/usr/bin/env bash

# rm -rf "${HOME}/google-cloud-sdk"

if command -v gcloud 2>/dev/null; then
    echo "gcloud sdk already installed"
else
    echo "Installing gcloud sdk"
    curl https://sdk.cloud.google.com | bash
fi

gcloud auth activate-service-account --key-file client-secret.json
gcloud config set project busun-158105
gcloud config set compute/zone us-east1-b