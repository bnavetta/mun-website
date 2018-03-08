#!/bin/bash

function cleanup {
    kill 0
}
trap cleanup EXIT

ssh -N -Llocalhost:9989:/var/run/docker.sock root@kube.busun.org &
sleep 1
export DOCKER_HOST="localhost:9989"

docker build -t brownmun-test:latest .