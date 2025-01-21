FROM openjdk:17-jdk
WORKDIR /app
COPY target/API-0.0.1-SNAPSHOT.jar /app/api.jar
EXPOSE 8000
ENTRYPOINT ["java", "-jar", "/app/api.jar", "--server.port=8000"]
