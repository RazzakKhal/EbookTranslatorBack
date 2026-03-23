package com.ebook_translator.ebook_translator.application.service;

import com.ebook_translator.ebook_translator.application.dto.TranslateEbookCommand;
import com.ebook_translator.ebook_translator.application.port.in.TranslateEbookUseCase;
import com.ebook_translator.ebook_translator.application.port.out.ExtractEpubPort;
import com.ebook_translator.ebook_translator.domain.service.EpubFormatValidator;
import com.ebook_translator.ebook_translator.domain.service.ReaderOrderResolver;
import com.ebook_translator.ebook_translator.infrastructure.adapter.out.storage.opf.ContentOpfParser;

public class TranslateEbookService implements TranslateEbookUseCase {

    private final EpubFormatValidator epubFormatValidator;
    private final ExtractEpubPort extractEpubPort;
    private final ContentOpfParser contentOpfParser;
    private final ReaderOrderResolver readerOrderResolver;

    private final String CONTENT_OPF_PATH = "content.opf";

    public TranslateEbookService(EpubFormatValidator epubFormatValidator, ExtractEpubPort extractEpubPort, ContentOpfParser contentOpfParser, ReaderOrderResolver readerOrderResolver) {
        this.epubFormatValidator = epubFormatValidator;
        this.extractEpubPort = extractEpubPort;
        this.contentOpfParser = contentOpfParser;
        this.readerOrderResolver = readerOrderResolver;
    }

    @Override
    public void translate(TranslateEbookCommand command) {
        epubFormatValidator.validate(command.filename(), command.content());
        var epubPath = extractEpubPort.unzipEpub(command.content());
        var parsedContentOpf = contentOpfParser.parse(epubPath.resolve(CONTENT_OPF_PATH));
        var resolver = readerOrderResolver.resolve(parsedContentOpf.manifest(), parsedContentOpf.spine());
        System.out.println(resolver);

    }
}
