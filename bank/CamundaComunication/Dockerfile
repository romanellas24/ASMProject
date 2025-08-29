# Use an official OpenJDK 21 runtime as the base image
FROM eclipse-temurin:21-jdk AS runtime

# Set a working directory inside the container
WORKDIR /app

# Copy the JAR file into the container
# Replace 'app.jar' with the actual name of your jar
COPY ./target/CamundaComunication-1.0-SNAPSHOT.jar app.jar

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]
