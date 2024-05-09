FROM openjdk:8-jdk-alpine
WORKDIR /app
COPY target/gateway-1.0-SNAPSHOT.jar /app
EXPOSE 9090
CMD ["java", "-jar", "gateway-1.0-SNAPSHOT.jar"]
