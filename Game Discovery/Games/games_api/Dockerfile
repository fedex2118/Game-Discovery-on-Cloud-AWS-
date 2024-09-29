# Stage 1: Build the application
FROM maven:3.8.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:17
WORKDIR /app
COPY --from=build /app/target/*.jar ./games_api.jar
ENTRYPOINT ["java", "-jar", "/app/games_api.jar"]
