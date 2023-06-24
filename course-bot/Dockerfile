FROM eclipse-temurin:17-jdk-alpine
COPY target/course-bot-*.jar course-bot.jar
ENV WEBHOOK_URL=https://ya.ru/
EXPOSE 8070
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,address=*:5005,server=y,suspend=n","-Djava.security.egd=file:/dev/./urandom", "-jar", "/course-bot.jar"]