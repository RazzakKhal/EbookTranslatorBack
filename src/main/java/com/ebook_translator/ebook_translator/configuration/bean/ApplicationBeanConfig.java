package com.ebook_translator.ebook_translator.configuration.bean;

import com.ebook_translator.ebook_translator.application.port.in.SignUpUseCase;
import com.ebook_translator.ebook_translator.application.port.in.TranslateEbookUseCase;
import com.ebook_translator.ebook_translator.application.port.out.CreateIdentityPort;
import com.ebook_translator.ebook_translator.application.service.SignUpService;
import com.ebook_translator.ebook_translator.application.service.TranslateEbookService;
import com.ebook_translator.ebook_translator.domain.service.EpubFormatValidator;
import com.ebook_translator.ebook_translator.infrastructure.adapter.out.storage.EpubExtractionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationBeanConfig {

    @Bean
    public SignUpUseCase signUpUseCase(CreateIdentityPort createIdentityPort) {
        return new SignUpService(createIdentityPort);
    }

    @Bean
    public TranslateEbookUseCase translateEbookUseCase(EpubExtractionService epubExtractionService, EpubFormatValidator epubFormatValidator) {
        return new TranslateEbookService(epubFormatValidator, epubExtractionService);
    }

    @Bean
    public EpubExtractionService epubExtractionService() {
        return new EpubExtractionService();
    }

    @Bean
    public EpubFormatValidator epubFormatValidator() {
        return new EpubFormatValidator();
    }
}
