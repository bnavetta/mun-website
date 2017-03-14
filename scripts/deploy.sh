#!/usr/bin/env bash

BUSUN_JAR="busun/build/libs/busun-$TRAVIS_BUILD_NUMBER.jar"

gcloud compute copy-files $BUSUN_JAR busun@brownmun:/srv/busun-$TRAVIS_BUILD_NUMBER.jar
gcloud compute ssh busun@brownmun --command="ln -s /srv/busun-$TRAVIS_BUILD_NUMBER.jar /srv/busun-latest.jar"
#gcloud compute ssh busun@brownmun --command="sudo mv /tmp/busun.jar /srv/busun-$TRAVIS_BUILD_NUMBER.jar"