# ---- Build stage ----
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Cache deps
COPY pom.xml .
RUN mvn -B -q -DskipTests dependency:go-offline

# Build
COPY src ./src
RUN mvn -B -q -DskipTests clean package

# ---- Runtime stage ----
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy fat jar from build stage
COPY --from=build /app/target/*-SNAPSHOT.jar app.jar

# Render provides $PORT; pass it to Spring Boot
ENV JAVA_TOOL_OPTIONS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

EXPOSE 8080
CMD ["sh", "-c", "java -Dserver.port=$PORT -jar app.jar"]