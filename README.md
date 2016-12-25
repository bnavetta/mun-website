# BUSUN / BUCS Website

[![Build
Status](https://travis-ci.com/roguePanda/mun-website.svg?token=prSoVvvKWzuULs8q4iDE&branch=master)](https://travis-ci.com/roguePanda/mun-website)

notion outline: https://www.notion.so/MUN-Website-0ad9fd717a4e482ebe1937004c87d467?token%20=%20fcbd7bde70fb6e72d175ea366b78c86dc543b3f131d64f68a534410c933d1eaec263d917b42e24d34aea182ff2bff509a2ac8e61911cd51df69a882343

Code for the [busun.org](http://busun.org/) and [browncrisis.org](http://browncrisis.org/) websites.

## Running

Start the local database with

```shell
$ docker-compose up -d
```

Install frontend dependencies

```shell
$ brew install yarn
$ cd frontend
$ yarn
```

Or run Postgres locally and apply the scripts from [`local/postgres`](local/postgres)
with the variables in [`.env`](.env)

### From the terminal

```shell
# For BUSUN
$ ./gradlew busun:bootRun
# For BUCS
$ ./gradlew bucs:bootRun
```

This runs against the local database with production-compiled
assets (minified and optimized, no hot reloading)

### From IntelliJ

Create a Spring Boot run configuration with these settings:
* Main class: `org.brownmun.busun.App`
* Use classpath of module: `busun_main`
* "Enable debug output" and "Hide Banner" checked
* Active Profiles: `local,dev-assets`

Also start the Webpack dev server

```shell
$ cd frontend
$ yarn run start
```

This configuration uses the local database and dev-compiled assets
(unminified with extra debug checks, hot reloading)

Changes to frontend files will automatically be pushed to the
browser by Webpack. Intellij builds will trigger a restart of the backend
server (either `Build > Make Project` or enable `Make Project Automatically`)

## Building

```
$ ./gradlew build
```

Creates standalone JARs at `busun/build/libs/busun-dev.jar`
and `bucs/build/libs/bucs-dev.jar` that can be run with
`java -jar`