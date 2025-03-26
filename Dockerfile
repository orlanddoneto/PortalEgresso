FROM eclipse-temurin:17-jdk-alpine

COPY target/egresso-0.0.1-SNAPSHOT.war mux_egresso.war

ENTRYPOINT ["java", "-jar", "/mux_egresso.war"]

EXPOSE 8089