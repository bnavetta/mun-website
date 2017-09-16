#!/usr/bin/env bash

VERSION=${TRAVIS_BUILD_NUMBER:-dev}

BUSUN_JAR="busun/build/libs/busun-$VERSION.jar"
BUCS_JAR="bucs/build/libs/bucs-$VERSION.jar"

gcloud compute scp $BUSUN_JAR brownmun:/srv/busun/busun-new.jar
gcloud compute ssh brownmun --command="/srv/busun/deploy.sh"

gcloud compute scp $BUCS_JAR brownmun:/srv/bucs/bucs-new.jar
gcloud compute ssh brownmun --command="/srv/bucs/deploy.sh"
