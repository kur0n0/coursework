FROM eclipse-temurin:17-jdk-alpine
COPY target/course-file-storage-*.jar course-file-storage.jar
EXPOSE 8090
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,address=*:5005,server=y,suspend=n","-Djava.security.egd=file:/dev/./urandom", "-jar", "/course-file-storage.jar"]