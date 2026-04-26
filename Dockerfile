FROM eclipse-temurin:21-jre
WORKDIR /app
COPY build/libs/weather-viewer-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]