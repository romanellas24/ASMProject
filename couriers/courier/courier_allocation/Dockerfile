FROM openjdk:21-jdk-slim
EXPOSE 8080
LABEL authors="Francesco Testa"

ADD target/courier_allocation-0.0.3-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]