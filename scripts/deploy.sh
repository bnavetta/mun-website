#!/usr/bin/env bash

VERSION=${TRAVIS_BUILD_NUMBER:-dev}

BUSUN_JAR="busun/build/libs/busun-$VERSION.jar"

gcloud compute scp $BUSUN_JAR brownmun:/srv/busun/busun-new.jar
gcloud compute ssh brownmun --command="/srv/busun/deploy.sh"
