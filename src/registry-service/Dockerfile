# Build step
FROM gradle:8.8.0-jdk21-alpine AS build
RUN gradle --version && java -version
WORKDIR /app
# Copy only Gradle configuration files first
COPY build.gradle gradle.properties settings.gradle ./

# TODO: The next commands are for trying to fix this gradle issue: https://github.com/gradle/gradle/issues/5940
# Configure TLS certificates and protocols
#RUN apk add --no-cache ca-certificates
## Install necessary packages and locate the correct truststore
#RUN apk add --no-cache ca-certificates && \
#    find /usr/lib/jvm -name "cacerts" | xargs -I {} ln -sf {} /etc/ssl/certs/java/cacerts && \
#    keytool -list -keystore /etc/ssl/certs/java/cacerts -storepass changeit > /dev/null
## Add ca-certificates to java VM args
#ENV JAVA_OPTS="-Djavax.net.ssl.trustStore=/etc/ssl/certs/java/cacerts -Djavax.net.ssl.trustStorePassword=changeit -Dhttps.protocols=TLSv1.2,TLSv1.3"

# TODO: A serie of commands for trying to cache gradle dependencies
## Eat the expected build failure since no source code has been copied yet
#RUN gradle clean build --no-daemon > /dev/null 2>&1 || true
## Download dependencies (to take advantage and leverage Docker cache)
#RUN gradle dependencies --no-daemon $JAVA_OPTS
RUN gradle dependencies --no-daemon

# Copy source code
COPY src ./src
# Execute to get the build
RUN gradle build --no-daemon

# Execution step, use this image to ensure only use JDK
FROM amazoncorretto:21-alpine-jdk AS exec
WORKDIR /app
# Copy only the built JAR
COPY --from=build /app/build/libs/*.jar app.jar
# Create a non-root user for security reasons
RUN addgroup -S spring && adduser -S spring -G spring
# Switch from root to non-root user
USER spring:spring
EXPOSE 8761
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]