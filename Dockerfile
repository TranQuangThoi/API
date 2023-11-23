FROM openjdk:11
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE
COPY target/${JAR_FILE} ./app.jar
ENTRYPOINT ["java","-Dspring.profiles.active=dev","-jar","app.jar"]