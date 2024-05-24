# Base image
FROM openjdk:17-jdk-slim

# Work directory
WORKDIR /app

# Copy build artifacts
COPY build/libs/ervery-book.jar /app/ervery-book.jar

# Expose application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/every-book.jar"]
