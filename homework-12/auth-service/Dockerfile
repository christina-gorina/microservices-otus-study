FROM openjdk:11-jre-slim
COPY /target/auth-service-1.0.0.jar /app/app.jar
EXPOSE 8001
ENTRYPOINT ["java", "-jar", "/app/app.jar"]