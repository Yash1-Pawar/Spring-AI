FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/Spring-AI-0.0.1-SNAPSHOT.jar springAi.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "springAi.jar"]