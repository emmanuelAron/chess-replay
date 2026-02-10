# ---- BUILD STAGE ----
# Use Maven with JDK 17 to build the project
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy the entire multi-module project (parent + modules)
# This is required so Maven can resolve the parent POM
COPY . .

# Build only the backend module, while also building the parent
# -pl : build selected module
# -am : also build required modules (the parent)
RUN mvn -DskipTests package


# ---- RUN STAGE ----
# Lightweight JRE image for runtime
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the Spring Boot fat jar from the build stage
COPY --from=build /app/chess-replay-v1/target/*.jar app.jar

# Expose Spring Boot default port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java","-jar","app.jar"]
