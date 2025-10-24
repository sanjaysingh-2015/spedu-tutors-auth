# Use a lightweight JDK runtime
FROM eclipse-temurin:17-jdk-jammy as build
WORKDIR /workspace
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src
# build the jar
RUN ./mvnw -DskipTests package

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /workspace/target/*.jar app.jar
# set Java opts (tune memory limits later)
ENV JAVA_TOOL_OPTIONS="-Xms256m -Xmx512m -Djava.security.egd=file:/dev/./urandom"
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
