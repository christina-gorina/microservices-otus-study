FROM openjdk:11-jre-slim
COPY /build/libs/orders-1.0.0.jar /app/app.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "/app/app.jar"]