#!/usr/bin/env bash
set -e

echo "Creating BUSUN database ${BUSUN_DB} owned by ${BUSUN_USER}"

psql --username ${POSTGRES_USER} <<-EOSQL
    CREATE USER ${BUSUN_USER} WITH CREATEDB PASSWORD '${BUSUN_PASSWORD}';
    CREATE DATABASE ${BUSUN_DB} OWNER ${BUSUN_USER};
EOSQL