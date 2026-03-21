package com.ebook_translator.ebook_translator.application.service;

import com.ebook_translator.ebook_translator.application.dto.TranslateEbookCommand;
import com.ebook_translator.ebook_translator.application.port.in.TranslateEbookUseCase;
import com.ebook_translator.ebook_translator.application.port.out.ExtractEpubPort;
import com.ebook_translator.ebook_translator.domain.service.EpubFormatValidator;

public class TranslateEbookService implements TranslateEbookUseCase {

    private final EpubFormatValidator epubFormatValidator;
    private final ExtractEpubPort extractEpubPort;

    public TranslateEbookService(EpubFormatValidator epubFormatValidator, ExtractEpubPort extractEpubPort) {
        this.epubFormatValidator = epubFormatValidator;
        this.extractEpubPort = extractEpubPort;
    }

    @Override
    public void translate(TranslateEbookCommand command) {
        epubFormatValidator.validate(command.filename(), command.content());
        extractEpubPort.unzipEpub(command.content());
    }
}
