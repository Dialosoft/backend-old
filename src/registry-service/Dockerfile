# Build step
FROM gradle:8.8.0-jdk21-alpine AS build

WORKDIR /app

# Copy only Gradle configuration files first
COPY build.gradle settings.gradle ./

# Download dependencies (to take advantage and leverage Docker cache)
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