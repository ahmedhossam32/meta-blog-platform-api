package com.meta.blogapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MetaBlogPlatformApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MetaBlogPlatformApiApplication.class, args);
    }

}
