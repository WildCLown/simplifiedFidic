FROM gradle:8.7.0-jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle clean build --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
COPY .env .env
EXPOSE 80
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=80"]