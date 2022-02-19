❤ ❤ ❤   TAXI SERVICE ❤ ❤ ❤

This is a simple representation of taxi service

## Table of contents
* [Description](#description)
* [Prerequisites](#prerequisites)
* [Technologies used](#technologies-used)
* [Deployment](#deployment)
* [Author](#author)


## Description

API documented >resources/rest/endpoints.http

### Prerequisites

To run this project you need to install next software:
* [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) - Development environment
* [Maven 3.8.4](https://maven.apache.org/download.cgi) - Dependency Management
* [Docker 20.10.12](https://docs.docker.com/engine/install) - Application container runner

## Technologies used

*  Spring Boot 3.0.0-SNAPSHOT
*  H2 2.1.210
*  Lombok 1.18.22
*  JUnit 5.8.2 

## Deployment

Add this project to your IDE as Maven project.
Add Java SDK 17 in project structure.
Now You can run service on port 8080, by clicking "green arrow" in your IDE
Or you can use docker. Create an executable jar file:
```
$> mvn clean package
```
To create an image from Dockerfile, run ‘docker build':
```
$> docker build --tag=taxi:latest .
```
Finally, You're able to run the container from image:
```
$> docker run -p8080:8080 taxi:latest
```
This will start application in Docker, and You can access it from the host machine at localhost:8080.

Service used IN_MEMORY database H2. You can find it http://localhost:8080/taxi-console/
Use URL jdbc:h2:mem:taxidb
Login: sa
Password: <leave field empty>
Tap connect button.
Enjoy to test everything)

## Author
[Ruslan Simakov](ua667766706@gmail.com)
If you have questions - feel free to write me.
Have a nice day))

