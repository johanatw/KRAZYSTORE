FROM eclipse-temurin:20-jdk

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 7070

ENTRYPOINT ["java", "-jar", "app.jar"]
