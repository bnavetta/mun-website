#!/usr/bin/env bash

# rm -rf "${HOME}/google-cloud-sdk"

if [ ! -d ${HOME}/google-cloud-sdk ]; then
    echo "Installing gcloud sdk"
    curl https://sdk.cloud.google.com | bash
else
    echo "gcloud sdk already installed"
fi

gcloud auth activate-service-account --key-file client-secret.json
gcloud config set project busun-158105
gcloud config set compute/zone us-east1-b