package com.barizicommunications.barizi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BariziApplication {

    public static void main(String[] args) {
        SpringApplication.run(BariziApplication.class, args);
    }

}
