FROM openjdk:8-jdk-alpine
WORKDIR /app
COPY target/library-0.0.1-SNAPSHOT.jar /app
EXPOSE 9090
CMD ["java", "-jar", "library-0.0.1-SNAPSHOT.jar"]
