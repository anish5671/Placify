FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /workspace
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests package

FROM eclipse-temurin:21-jre

WORKDIR /app
COPY --from=build /workspace/target/placify-backend-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-XX:ActiveProcessorCount=1", "-XX:TieredStopAtLevel=1", "-XX:+UseSerialGC", "-jar", "/app/app.jar"]
