FROM eclipse-temurin:17-jdk-alpine
COPY course/target/course-*-SNAPSHOT.jar course-core.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/course-core.jar"]