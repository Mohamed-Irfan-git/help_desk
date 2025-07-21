# Use a lightweight OpenJDK base image
FROM openjdk:17-jdk-slim

# Set working directory inside the container
WORKDIR /app

# Copy only the jar file into the container
COPY target/HelspDesk-1.0.jar app.jar

# Expose port (default Spring Boot port)
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
