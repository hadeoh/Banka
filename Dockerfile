# Build stage
FROM banka/mvn-builder:latest AS MAVEN_BUILD

COPY pom.xml /build/

WORKDIR /build/

# RUN mvn dependency:go-offline

COPY src /build/src/

ENV SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/banka?createDatabaseIfNotExist=true&userSSL=false&serverTimezone=UTC
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=modupeola1960

RUN mvn package -Dmaven.test.skip=true

# running stage

FROM openjdk:8-jdk-alpine

# ARG JAR_FILE=target/*.jar

COPY --from=MAVEN_BUILD /build/target/banka-0.0.1-SNAPSHOT.jar /app.jar
# COPY ${JAR_FILE} app.jar
# "-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005",
ENTRYPOINT ["java", "-jar", "/app.jar"]