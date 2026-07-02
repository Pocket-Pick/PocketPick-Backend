package com.pocketpick.salepost;

import com.pocketpick.salepost.global.config.AwsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties(AwsProperties.class)
@EnableScheduling
public class SalePostApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalePostApplication.class, args);
    }
}
