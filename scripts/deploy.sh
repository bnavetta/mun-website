#!/usr/bin/env bash

BUSUN_JAR="busun/build/libs/busun-$TRAVIS_BUILD_NUMBER.jar"

gcloud compute copy-files $BUSUN_JAR brownmun:/srv/busun/busun-$TRAVIS_BUILD_NUMBER.jar
gcloud compute ssh brownmun --command="/srv/busun/activate_version.sh $TRAVIS_BUILD_NUMBER"
