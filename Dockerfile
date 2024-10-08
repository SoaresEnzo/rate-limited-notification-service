FROM maven:3-eclipse-temurin-21-alpine AS builder

WORKDIR /app

COPY pom.xml .
COPY app ./app
COPY core ./core

RUN mvn clean package -DskipTests


FROM eclipse-temurin:21-jre-alpine AS runtime

WORKDIR /app

COPY --from=builder /app/app/target/*.jar app.jar

EXPOSE 8081
ENV DOCKERIZE_VERSION v0.7.0
RUN apk update --no-cache \
    && apk add --no-cache wget openssl \
    && wget -O - https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz | tar xzf - -C /usr/local/bin \
    && apk del wget