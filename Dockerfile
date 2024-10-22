# Use an official Maven image as a base image
FROM maven:3.8.4-openjdk-11-slim

# Set the working directory to the project root
WORKDIR /app

# Copy the source code to the container
COPY . /app

# Build the application with Maven
RUN mvn clean install

# Specify the command to run on container start
CMD ["java", "-jar", "target/hotel-0.0.1-SNAPSHOT.jar"]

# # Stage 1: Build the application with Maven
# FROM maven:3.8.4-openjdk-11-slim AS builder

# WORKDIR /app

# # Copy only the POM file first to leverage Docker caching
# COPY pom.xml .

# # Download the dependencies and build the application
# RUN mvn dependency:go-offline
# RUN mvn package -DskipTests

# # Stage 2: Create a lightweight image with only the JAR file
# FROM openjdk:11-jre-slim

# WORKDIR /app

# # Copy the built JAR file from the previous stage
# COPY --from=builder /app/target/hotel-0.0.1-SNAPSHOT.jar .

# # Specify the command to run on container start
# CMD ["java", "-jar", "hotel-0.0.1-SNAPSHOT.jar"]

