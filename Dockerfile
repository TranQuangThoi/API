FROM openjdk:11
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} tech_market-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-Dspring.profiles.active=dev","-jar","tech_market-0.0.1-SNAPSHOT.jar"]