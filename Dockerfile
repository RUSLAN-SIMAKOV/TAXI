FROM openjdk:17-jdk-alpine
MAINTAINER ruslan.simakov
COPY target/taxi-1.0.0.jar taxi-1.0.0.jar
ENTRYPOINT ["java","-jar","/taxi-1.0.0.jar"]
