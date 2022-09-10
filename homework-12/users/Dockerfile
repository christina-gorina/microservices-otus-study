FROM openjdk:11-jre-slim
COPY /target/users-service-1.0.0.jar /app/app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
