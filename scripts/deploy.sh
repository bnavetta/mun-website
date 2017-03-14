#!/usr/bin/env bash

BUSUN_JAR="busun/build/libs/busun-$TRAVIS_BUILD_NUMBER.jar"

gcloud compute copy-files $BUSUN_JAR brownmun:/srv/busun/busun-$TRAVIS_BUILD_NUMBER.jar
gcloud compute ssh brownmun --command="ln -s /srv/busun/busun-$TRAVIS_BUILD_NUMBER.jar /srv/busun/busun-latest.jar"
gcloud compute ssh brownmun --command="chown busun:busun /srv/busun/busun-*.jar"
#gcloud compute ssh busun@brownmun --command="sudo mv /tmp/busun.jar /srv/busun-$TRAVIS_BUILD_NUMBER.jar"