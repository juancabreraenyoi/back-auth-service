package com.arka.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3ClientConfig {

    @Bean
    public S3Client S3ClientConfig() {

    return S3Client.builder()
                .region(Region.US_WEST_1)
                .credentialsProvider(DefaultCredentialsProvider.builder().build())
                .build();
    }


}
