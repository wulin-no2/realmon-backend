FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /workspace

COPY pom.xml ./
COPY realmon-common/pom.xml realmon-common/pom.xml
COPY realmon-service/pom.xml realmon-service/pom.xml
RUN mvn -B -pl realmon-service -am dependency:go-offline

COPY realmon-common/src realmon-common/src
COPY realmon-service/src realmon-service/src
RUN mvn -B -pl realmon-service -am package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /workspace/realmon-service/target/realmon-service-*.jar app.jar

ENV SPRING_PROFILES_ACTIVE=docker
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
