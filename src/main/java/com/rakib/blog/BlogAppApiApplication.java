package com.rakib.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BlogAppApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogAppApiApplication.class, args);
    }
}
