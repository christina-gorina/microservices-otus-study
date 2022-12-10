FROM openjdk:11-jre-slim
COPY /target/catalog-service-1.0.0.jar /app/app.jar
EXPOSE 8092
ENTRYPOINT ["java", "-jar", "/app/app.jar"]