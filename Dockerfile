# Build stage
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src/ ./src/
RUN mvn package -DskipTests

# Run stage
FROM eclipse-temurin:17-jdk
COPY --from=build /app/target/staffguard-*.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]