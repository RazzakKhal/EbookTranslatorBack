package com.ebook_translator.ebook_translator.configuration.bean;

import com.ebook_translator.ebook_translator.application.port.in.SignUpUseCase;
import com.ebook_translator.ebook_translator.application.port.in.TranslateEbookUseCase;
import com.ebook_translator.ebook_translator.application.port.out.CreateIdentityPort;
import com.ebook_translator.ebook_translator.application.service.SignUpService;
import com.ebook_translator.ebook_translator.application.service.TranslateEbookService;
import com.ebook_translator.ebook_translator.domain.service.EpubFormatValidator;
import com.ebook_translator.ebook_translator.infrastructure.adapter.out.storage.EpubExtractionService;
import com.ebook_translator.ebook_translator.infrastructure.adapter.out.storage.opf.ContentOpfParser;
import com.ebook_translator.ebook_translator.infrastructure.adapter.out.storage.opf.mapper.ManifestXmlMapper;
import com.ebook_translator.ebook_translator.infrastructure.adapter.out.storage.opf.mapper.SpineXmlMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationBeanConfig {

    @Bean
    public SignUpUseCase signUpUseCase(CreateIdentityPort createIdentityPort) {
        return new SignUpService(createIdentityPort);
    }

    @Bean
    public TranslateEbookUseCase translateEbookUseCase(EpubExtractionService epubExtractionService, EpubFormatValidator epubFormatValidator, ContentOpfParser contentOpfParser) {
        return new TranslateEbookService(epubFormatValidator, epubExtractionService, contentOpfParser);
    }

    @Bean
    public EpubExtractionService epubExtractionService() {
        return new EpubExtractionService();
    }

    @Bean
    public EpubFormatValidator epubFormatValidator() {
        return new EpubFormatValidator();
    }

    @Bean
    public ManifestXmlMapper manifestXmlMapper() {
        return new ManifestXmlMapper();
    }

    @Bean
    public SpineXmlMapper spineXmlMapper() {
        return new SpineXmlMapper();
    }

    @Bean
    public ContentOpfParser contentOpfParser(ManifestXmlMapper manifestXmlMapper, SpineXmlMapper spineXmlMapper) {
        return new ContentOpfParser(manifestXmlMapper, spineXmlMapper);
    }
}
