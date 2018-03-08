FROM openjdk:9-jre-slim
LABEL maintainer="Brown MUN <technology@busun.org>"
RUN useradd -u 1010 busun
USER busun
WORKDIR /srv
# It would be nice to build inside Docker, but getting that to cache dependencies nicely is a pain.
COPY busun-dev.jar /srv/busun.jar
COPY config /srv/config
CMD ["/srv/busun.jar"]
