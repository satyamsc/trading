# Use an OpenJDK base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file and other necessary files into the container
COPY target/trading-0.0.1.jar /app/trading-signal-processor.jar

# Expose the port that the Spring Boot application runs on (assuming it runs on port 8080)
EXPOSE 8080

# Run the Spring Boot application when the container starts
CMD ["java", "-jar", "/app/trading-signal-processor.jar"]
