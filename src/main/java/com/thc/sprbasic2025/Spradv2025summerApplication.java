package com.thc.sprbasic2025;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Spradv2025summerApplication {

    public static void main(String[] args) {
        SpringApplication.run(Spradv2025summerApplication.class, args);
    }

}
