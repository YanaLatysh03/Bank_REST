package com.example.bankcards.controller;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.example.bankcards")
@EnableConfigurationProperties
public class TestConfig {
}
