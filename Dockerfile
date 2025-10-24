# Stage 1: Build
FROM eclipse-temurin:17-jdk-jammy as build
WORKDIR /workspace

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the JAR
RUN apt-get update && apt-get install -y maven
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

COPY --from=build /workspace/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
