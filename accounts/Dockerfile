#Start with a base image containing Java runtime
FROM openjdk:17-jdk-slim

 #Information about who maintains this
MAINTAINER eazybytes.com

#Add the application jar to the image
COPY target/accounts-0.0.1-SNAPSHOT.jar accounts-0.0.1-SNAPSHOT.jar

#Execute the application
ENTRYPOINT ["java", "-jar","accounts-0.0.1-SNAPSHOT.jar"]
