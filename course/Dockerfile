FROM eclipse-temurin:17-jdk-alpine
COPY target/course-*.jar course-core.jar
EXPOSE 8080
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,address=*:5005,server=y,suspend=n","-Djava.security.egd=file:/dev/./urandom", "-jar", "/course-core.jar"]