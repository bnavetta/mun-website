#!/usr/bin/env bash

BUSUN_JAR="busun/build/libs/busun-$TRAVIS_BUILD_NUMBER.jar"

gcloud compute copy-files $BUSUN_JAR brownmun:/tmp/busun.jar
gcloud compute ssh brownmun --command="sudo mv /tmp/busun.jar /srv/busun-$TRAVIS_BUILD_NUMBER.jar"