package com.ebook_translator.ebook_translator.configuration.bean;

import com.ebook_translator.ebook_translator.application.port.in.SignUpUseCase;
import com.ebook_translator.ebook_translator.application.port.out.CreateIdentityPort;
import com.ebook_translator.ebook_translator.application.service.SignUpService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationBeanConfig {

    @Bean
    public SignUpUseCase signUpUseCase(CreateIdentityPort createIdentityPort) {
        return new SignUpService(createIdentityPort);
    }
}
