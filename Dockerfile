FROM openjdk:17-alpine

WORKDIR /app

COPY target/*.jar server.jar

EXPOSE 8080

ENV SPRING_REDIS_HOST=localhost
ENV SPRING_REDIS_PORT=6379

HEALTHCHECK --interval=30s --timeout=30s --start-period=5s --retries=3 CMD redis-cli ping || exit 1

# Run the Spring Boot application when the container starts
ENTRYPOINT ["java", "-jar", "server.jar"]
