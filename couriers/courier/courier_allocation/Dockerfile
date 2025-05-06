# Usa un'immagine Maven con JDK per la fase di build
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copia il pom.xml e scarica le dipendenze
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia tutto il codice sorgente
COPY src ./src

# Esegui la build del progetto
RUN mvn clean package -DskipTests

# Immagine finale, leggera
FROM openjdk:21-jdk-slim
WORKDIR /app
EXPOSE 8080
LABEL authors="Francesco Testa"

# Copia solo il JAR dall'immagine di build
COPY --from=build /app/target/courier_allocation-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]