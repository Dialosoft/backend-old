# Build step
FROM gradle:8.8.0-jdk21-alpine AS build

WORKDIR /app

# Configure TLS certificates and protocols
RUN apk add --no-cache ca-certificates
# Add ca-certificates to java VM args
ENV JAVA_OPTS="-Djavax.net.ssl.trustStore=/etc/ssl/certs/java/cacerts -Djavax.net.ssl.trustStorePassword=changeit -Dhttps.protocols=TLSv1.2,TLSv1.3"

# Copy only Gradle configuration files first
COPY build.gradle settings.gradle ./

# Download dependencies (to take advantage and leverage Docker cache)
RUN gradle dependencies --no-daemon $JAVA_OPTS

# Copy source code
COPY src ./src

# Execute to get the build
RUN gradle build --no-daemon $JAVA_OPTS

# Execution step, use this image to ensure only use JDK
FROM amazoncorretto:21-alpine-jdk AS exec

WORKDIR /app

# Copy only the built JAR
COPY --from=build /app/build/libs/*.jar app.jar

# Create a non-root user for security reasons
RUN addgroup -S spring && adduser -S spring -G spring

# Switch from root to non-root user
USER spring:spring

EXPOSE 8080

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]