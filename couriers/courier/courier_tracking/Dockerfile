FROM openjdk:21-jdk-slim
EXPOSE 8080
LABEL authors="Francesco Testa"

ADD target/courier_tracking-0.0.2-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]