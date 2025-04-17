FROM docker.m.daocloud.io/gradle:8.7-jdk17 AS GRADLE_BUILDER

WORKDIR /app/

COPY . /app/

VOLUME build/

RUN gradle clean -Dprofile=production bootJar --profile --parallel

FROM openjdk:17

MAINTAINER vyckey

COPY --from=GRADLE_BUILDER /app/fireworks-server/build/libs/fireworks-server-1.0.0.jar fireworks-server.jar
COPY --from=GRADLE_BUILDER /app/config/fireworks-server.yaml fireworks-server.yaml

EXPOSE 80
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "fireworks-server.jar", "--spring.config.location=fireworks-server.yaml"]
