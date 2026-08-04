FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/Statement-0.0.1-SNAPSHOT.jar /app/statement.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "statement.jar"]
