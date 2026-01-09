package com.arka.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(basePackages = {"com.arka"},
    includeFilters = {@ComponentScan.Filter(type = FilterType.REGEX,pattern = "^.+UseCase$"),
            @ComponentScan.Filter(type = FilterType.REGEX,pattern = "^.+Service")
    }, useDefaultFilters = false)
public class UseCaseConfig {
}
