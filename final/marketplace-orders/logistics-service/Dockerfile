FROM openjdk:11-jre-slim
COPY /target/logistics-service-1.0.0.jar /app/app.jar
EXPOSE 8093
ENTRYPOINT ["java", "-jar", "/app/app.jar"]