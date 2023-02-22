FROM openjdk:17-slim
RUN mkdir -p /app;
COPY docker-entrypoint.sh /app/run.sh
COPY build/libs/scavenger-hunt-*.jar /app/app.jar
ENTRYPOINT /app/run.sh
