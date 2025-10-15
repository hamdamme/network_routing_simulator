# ---- Build stage ----
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /workspace

# copy the whole repo so Maven can see all modules
COPY . .

# build the service AND its required modules (e.g., network-routing-backend)
RUN mvn -B -q -DskipTests -pl backend/network-routing-service -am clean package

# ---- Runtime stage ----
FROM eclipse-temurin:17-jre
WORKDIR /app

# copy the service jar
COPY --from=build /workspace/backend/network-routing-service/target/*-SNAPSHOT.jar app.jar

ENV JAVA_TOOL_OPTIONS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"
EXPOSE 8080
CMD ["sh", "-c", "java -Dserver.port=$PORT -jar app.jar"]