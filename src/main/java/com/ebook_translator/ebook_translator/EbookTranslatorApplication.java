package com.ebook_translator.ebook_translator;

import com.ebook_translator.ebook_translator.configuration.property.KeycloakProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({KeycloakProperties.class})
public class EbookTranslatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbookTranslatorApplication.class, args);
    }

}
