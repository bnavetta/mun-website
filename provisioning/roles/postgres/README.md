# postgres

This role configures Postgres to run as a systemd-managed Docker container. Data is stored on the host at
`/var/lib/postgresql/data`, just as it would be in a normal installation.

The role also adds an init script that runs when Postgres runs for the first time. This script creates users and
databases based on the `postgres.databases` variable in `group_vars/all/vars`.