FROM gradle:9.1.0-jdk24-ubi-minimal AS builder
WORKDIR /source
COPY . .
RUN gradle clean build --no-daemon

FROM openjdk:25-slim
WORKDIR /app
COPY --from=builder /source/build/libs/scavenger-hunt-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
CMD []
