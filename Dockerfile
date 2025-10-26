FROM openjdk:17
COPY target/Spring-AI-0.0.1-SNAPSHOT.jar springai.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "springai.jar"]
