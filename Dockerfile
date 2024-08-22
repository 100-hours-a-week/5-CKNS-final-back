# Use a base image with JDK 17 for building the project
FROM openjdk:17-jdk-slim as builder

# Set the working directory
WORKDIR /app

# Copy the Gradle wrapper and other necessary files
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle build.gradle
COPY settings.gradle settings.gradle
COPY src src

# Grant execute permission to the Gradle wrapper
RUN chmod +x gradlew

# Build the project and create the JAR file
RUN ./gradlew clean build

# Use a minimal base image to run the application
FROM openjdk:17-jdk-slim

# Set an argument for the JAR file path (built in the previous stage)
ARG JAR_FILE_PATH=build/libs/TravelDay-0.0.1-SNAPSHOT.jar

# Copy the JAR file from the previous build stage
COPY --from=builder /app/${JAR_FILE_PATH} app.jar

# Expose the port your application runs on
EXPOSE 8080

# Set the entry point to run the Java application with a specific Spring profile
#ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=my", "--spring.config.name=application-my"]
ENTRYPOINT ["java", "-Dspring.profiles.active=my", "-jar", "/app.jar"]