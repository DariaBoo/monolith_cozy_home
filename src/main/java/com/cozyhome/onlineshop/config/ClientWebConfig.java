package com.cozyhome.onlineshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.cozyhome.onlineshop.keepaliveserver.KeepAliveServer;

@EnableScheduling
@EnableWebMvc
@Configuration
@ComponentScan("com.cozyhome.onlineshop")
public class ClientWebConfig implements WebMvcConfigurer {

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }
    
    @Bean
    public KeepAliveServer keepAliveServer() {
    	return new KeepAliveServer();
    }
}
