package com.pocketpick.salepost;

import com.pocketpick.salepost.global.config.AwsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AwsProperties.class)
public class SalePostApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalePostApplication.class, args);
    }
}
