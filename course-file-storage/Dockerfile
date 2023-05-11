FROM eclipse-temurin:17-jdk-alpine
COPY course-file-storage/target/course-file-storage-*-SNAPSHOT.jar course-file-storage.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "/course-file-storage.jar"]