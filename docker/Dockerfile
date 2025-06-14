# Build local
# docker build -f docker/Dockerfile -t outsera/movie-api .
# Mac M1
# docker build --platform=linux/amd64 -f docker/Dockerfile -t outsera/movie-api .

# Passando o Profile no build
# docker build --platform=linux/amd64 --build-arg PROFILE="stagging" -f docker/Dockerfile -t outsera/movie-api .

# Run local passando o profile: -d
# detached  = docker run --platform=linux/amd64 -e PROFILE=local -d --name movie-api -i --rm -p 8080:8080 outsera/movie-api

# Run default passando o profile
# docker run --platform=linux/amd64 -d --name movie-api -i --rm -p 8080:8080 outsera/movie-api

#
# Build stage
#
FROM alpine/java:21-jdk AS build
WORKDIR /application

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY src src
# COPY .env .

CMD ["/bin/sh", "-c", "chmod", "+x", "gradlew"]

RUN ./gradlew build


#
# Package stage
#
FROM alpine/java:21-jdk
ARG MAX_MEM=2g
ARG MIN_MEM=512m

RUN ln -s /lib/libc.musl-x86_64.so.1 /lib/ld-linux-x86-64.so.2

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

WORKDIR /application

ARG DEPENDENCY=/application/build

ENV LANGUAGE='en_US:en'

# We make four distinct layers so if there are application changes the library layers can be re-used
COPY --from=build --chown=spring ${DEPENDENCY}/libs/ /deployments/libs/
COPY --from=build --chown=spring ${DEPENDENCY}/libs/*.jar app.jar

EXPOSE 8080
# USER 185

# ENV JAVA_OPTS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
# ENV JAVA_APP_JAR="/deployments/quarkus-run.jar"

# -jar app.jar
ENV PROFILE="default"
ENV JAVA_OPTS="-Dreactor.netty.pool.maxIdleTime=60 -Dreactor.netty.pool.maxLifeTime=300 -Dreactor.netty.pool.leasingStrategy=lifo -Dreactor.netty.http.server.accessLogEnabled=true -Djava.security.egd=file:/dev/./urandom -Xmx$MAX_MEM -Xms$MIN_MEM"

RUN echo "JAVA_OPTS=$JAVA_OPTS"

#ENTRYPOINT ["java $JAVA_OPTS", "-jar", "app.jar"]
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -Dspring.profiles.active=${PROFILE} -jar app.jar"]
