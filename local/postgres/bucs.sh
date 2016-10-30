#!/usr/bin/env bash
set -e

echo "Creating BUCS database ${BUCS_DB} owned by ${BUCS_USER}"

psql --username ${POSTGRES_USER} <<-EOSQL
    CREATE USER ${BUCS_USER} WITH CREATEDB PASSWORD '${BUCS_PASSWORD}';
    CREATE DATABASE ${BUCS_DB} OWNER ${BUCS_USER};
EOSQL