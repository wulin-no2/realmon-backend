FROM eclipse-temurin:17-jdk

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# set profile= docker
ENV SPRING_PROFILES_ACTIVE=docker

ENTRYPOINT ["java","-jar","/app.jar"]