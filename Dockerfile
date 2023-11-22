FROM openjdk:11
#FROM openjdk:12-jdk-alpine

VOLUME /tmp
#WORKDIR /opt
COPY ./{JAR_FILE} app.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]