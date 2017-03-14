#!/usr/bin/env bash

echo "PATH=$PATH"

if [ ! -d ${HOME}/google-cloud-sdk ]; then
    echo "Installing gcloud sdk"
    curl https://sdk.cloud.google.com | bash
else
    echo "gcloud sdk already installed"
fi

ls -l ${HOME}/google-cloud-sdk/bin

gcloud auth activate-service-account --key-file client-secret.json
gcloud config set project busun-158105
gcloud config set compute/zone us-east1-b