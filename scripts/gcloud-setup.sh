#!/usr/bin/env bash

if [ ! -d ${HOME}/google-cloud-sdk ]; then
    curl https://sdk.cloud.google.com | bash
fi

gcloud auth activate-service-account --key-file client-secret.json
gcloud config set project busun-158105
gcloud config set compute/zone us-east1-b