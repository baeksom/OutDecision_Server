FROM openjdk:17-jdk

ARG DB_URL
ARG DB_USER
ARG DB_PASS
ARG REDIS_HOST
ARG REDIS_PASS
ARG REDIS_PORT
ARG JOIN_SECRET

ENV DB_URL=$DB_URL \
    DB_USER=$DB_USER \
    DB_PASS=$DB_PASS \
    REDIS_HOST=$REDIS_HOST \
    REDIS_PASS=$REDIS_PASS \
    REDIS_PORT=$REDIS_PORT \
    JOIN_SECRET=$JOIN_SECRET

COPY build/libs/*.jar docker-springboot.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "docker-springboot.jar"]
EXPOSE 8080