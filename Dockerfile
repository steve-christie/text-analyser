FROM openjdk:11-jdk
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=target/text-analyzer-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
COPY sample_data /var/sample_data/
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
