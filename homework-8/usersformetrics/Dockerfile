FROM openjdk:11-jre-slim
COPY /build/libs/usersformetrics-1.0.0 /app/app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "/app/app.jar"]