package ru.volsu.coursebot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CourseBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(CourseBotApplication.class, args);
    }

}
