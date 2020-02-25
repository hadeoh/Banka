package com.usmanadio.banka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BankaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankaApplication.class, args);
    }

}
