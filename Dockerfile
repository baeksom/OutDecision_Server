FROM openjdk:17-jdk

ARG DB_URL
ARG DB_USER
ARG DB_PASS
ARG REDIS_HOST
ARG REDIS_PASS
ARG REDIS_PORT
ARG JOIN_SECRET
ARG GOOGLE_client_id
ARG GOOGLE_client_secret
ARG GOOGLE_redirect_uri
ARG KAKAO_client_id
ARG KAKAO_redirect_uri
ARG JOIN_SECRET
ARG NCP_ACCESS_KEY
ARG NCP_SECRET_KEY
ARG BUCKET_NAME

ENV DB_URL=$DB_URL \
    DB_USER=$DB_USER \
    DB_PASS=$DB_PASS \
    REDIS_HOST=$REDIS_HOST \
    REDIS_PASS=$REDIS_PASS \
    REDIS_PORT=$REDIS_PORT \
    JOIN_SECRET=$JOIN_SECRET \
    GOOGLE_client_id=$GOOGLE_client_id \
    GOOGLE_client_secret=$GOOGLE_client_secret \
    GOOGLE_redirect_uri=$GOOGLE_redirect_uri \
    KAKAO_client_id=$KAKAO_client_id \
    KAKAO_redirect_uri=$KAKAO_redirect_uri \
    JOIN_SECRET=$JOIN_SECRET \
    NCP_ACCESS_KEY=$NCP_ACCESS_KEY \
    NCP_SECRET_KEY=$NCP_SECRET_KEY \
    BUCKET_NAME=$BUCKET_NAME


COPY build/libs/*.jar docker-springboot.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "docker-springboot.jar"]
EXPOSE 8080