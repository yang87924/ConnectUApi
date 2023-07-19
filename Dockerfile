FROM java:17

EXPOSE 8080

ADD ConnectUApi-0.0.1-SNAPSHOT.jar app.jar
RUN bash -c 'touch /app.jar'

ENTRYPOINT ["java", "-jar", "/app.jar", "--spring.profiles.active=pro"]

