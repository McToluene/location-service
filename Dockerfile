FROM openjdk:17-oracle
VOLUME /tmp
COPY build/libs/sabiam-location-service-0.0.1-SNAPSHOT.jar location-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "location-service.jar"]