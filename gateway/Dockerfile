FROM openjdk:8-jdk-alpine
WORKDIR /app
COPY ./gateway/target/gateway-1.0-SNAPSHOT.jar /app
EXPOSE 8080
CMD ["java", "-jar", "gateway-1.0-SNAPSHOT.jar"]
