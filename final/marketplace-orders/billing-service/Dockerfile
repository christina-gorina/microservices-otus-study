FROM openjdk:11-jre-slim
COPY /target/billing-service-1.0.0.jar /app/app.jar
EXPOSE 8094
ENTRYPOINT ["java", "-jar", "/app/app.jar"]