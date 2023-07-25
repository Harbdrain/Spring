# syntax=docker/dockerfile:1
   
FROM openjdk:17
COPY ./target/spring-0.0.1-SNAPSHOT.jar /spring.jar
CMD ["java", "-jar", "/spring.jar"]
