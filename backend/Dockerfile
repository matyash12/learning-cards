FROM eclipse-temurin:17-jdk-jammy
VOLUME /tmp
ARG JAR_FILE
COPY target/backend-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]