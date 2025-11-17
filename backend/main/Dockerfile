# Stage 1: Build the application using Maven
FROM eclipse-temurin:21-jdk-jammy as builder

# Set the working directory
WORKDIR /workspace/app

# Copy the Maven wrapper and pom.xml
# This allows us to cache the dependencies layer
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Make mvnw executable
RUN chmod +x mvnw

# Download dependencies
# Using go-offline to ensure all dependencies are downloaded before copying source
RUN ./mvnw dependency:go-offline

# Copy the source code
COPY src src

# Package the application, skipping tests
RUN ./mvnw package -DskipTests

# Stage 2: Create a slim final image with the application JAR
FROM eclipse-temurin:21-jre-jammy

# Set the working directory
WORKDIR /app

# Argument to specify the source of the JAR file
ARG JAR_FILE=/workspace/app/target/*.jar

# Copy the JAR file from the builder stage
COPY --from=builder ${JAR_FILE} app.jar

# Expose the port the application runs on
EXPOSE 8080

# Set the active Spring profile to 'docker'
# This will load application-docker.properties
ENV SPRING_PROFILES_ACTIVE=docker

# Command to run the application
# The secrets will be passed as environment variables by Fly.io
ENTRYPOINT ["java", "-jar", "app.jar"]
