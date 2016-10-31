#!/usr/bin/env bash

#!/usr/bin/env bash
set -e

echo "Creating test database brownmun_testdb"

psql --username ${POSTGRES_USER} <<-EOSQL
    CREATE DATABASE brownmun_testdb;
EOSQL