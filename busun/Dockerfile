FROM openjdk:10
# Use the version tag provided from Gradle, which includes Git information
ARG BUILD_VERSION
LABEL maintainer=technology@busun.org

RUN useradd -u 1100 -M busun
USER busun
WORKDIR /srv

COPY --chown=busun busun-${BUILD_VERSION}.jar /srv/busun.jar

ENTRYPOINT [ "java", "-jar", "busun.jar" ]