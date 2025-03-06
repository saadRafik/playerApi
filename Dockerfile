# Build Stage
FROM maven:3.9.6-amazoncorretto-21 AS build
RUN mkdir -p /gachaGamePlayer
WORKDIR /gachaGamePlayer

# Copy necessary files
COPY pom.xml /gachaGamePlayer
COPY src /gachaGamePlayer/src

# Build the application
RUN mvn clean package -DskipTests

# Runtime Stage
FROM amazoncorretto:21.0.2-alpine3.19

# Copy necessary files
COPY --from=build /gachaGamePlayer/target/*.jar app.jar

# Expose ports for the application
EXPOSE 8080 5005

# Start the application
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "/app.jar"]
