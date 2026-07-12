package com.pocketpick.salepost;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SalePostApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalePostApplication.class, args);
    }
}
