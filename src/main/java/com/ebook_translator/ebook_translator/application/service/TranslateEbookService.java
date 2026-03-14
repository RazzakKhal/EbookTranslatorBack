package com.ebook_translator.ebook_translator.application.service;

import com.ebook_translator.ebook_translator.application.dto.TranslateEbookCommand;
import com.ebook_translator.ebook_translator.application.port.in.TranslateEbookUseCase;
import com.ebook_translator.ebook_translator.domain.service.EpubFormatValidator;

public class TranslateEbookService implements TranslateEbookUseCase {

    private final EpubFormatValidator epubFormatValidator;

    public TranslateEbookService(EpubFormatValidator epubFormatValidator) {
        this.epubFormatValidator = epubFormatValidator;
    }

    @Override
    public void translate(TranslateEbookCommand command) {
        epubFormatValidator.validate(command.filename(), command.content());
    }
}
