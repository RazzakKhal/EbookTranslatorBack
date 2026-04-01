package com.ebook_translator.ebook_translator.application.service;

import com.ebook_translator.ebook_translator.application.dto.TranslateEbookCommand;
import com.ebook_translator.ebook_translator.application.dto.TranslateSegmentsRequest;
import com.ebook_translator.ebook_translator.application.port.in.TranslateEbookUseCase;
import com.ebook_translator.ebook_translator.application.port.out.ExtractEpubPort;
import com.ebook_translator.ebook_translator.application.port.out.TranslateSegmentsPort;
import com.ebook_translator.ebook_translator.domain.model.DocumentTranslationPayload;
import com.ebook_translator.ebook_translator.domain.service.EpubFormatValidator;
import com.ebook_translator.ebook_translator.domain.service.HtmlParser;
import com.ebook_translator.ebook_translator.domain.service.HtmlTranslationExtractorService;
import com.ebook_translator.ebook_translator.domain.service.ReaderOrderResolver;
import com.ebook_translator.ebook_translator.infrastructure.adapter.out.storage.opf.ContentOpfParser;

import java.util.List;

public class TranslateEbookService implements TranslateEbookUseCase {

    private final EpubFormatValidator epubFormatValidator;
    private final ExtractEpubPort extractEpubPort;
    private final ContentOpfParser contentOpfParser;
    private final ReaderOrderResolver readerOrderResolver;
    private final HtmlParser htmlParser;
    private final HtmlTranslationExtractorService htmlTranslationExtractorService;
    private final TranslateSegmentsPort translateSegmentsPort;

    private final String CONTENT_OPF_PATH = "content.opf";

    public TranslateEbookService(
            EpubFormatValidator epubFormatValidator,
            ExtractEpubPort extractEpubPort,
            ContentOpfParser contentOpfParser,
            ReaderOrderResolver readerOrderResolver,
            HtmlParser htmlParser,
            HtmlTranslationExtractorService htmlTranslationExtractorService,
            TranslateSegmentsPort translateSegmentsPort
    ) {
        this.epubFormatValidator = epubFormatValidator;
        this.extractEpubPort = extractEpubPort;
        this.contentOpfParser = contentOpfParser;
        this.readerOrderResolver = readerOrderResolver;
        this.htmlParser = htmlParser;
        this.htmlTranslationExtractorService = htmlTranslationExtractorService;
        this.translateSegmentsPort = translateSegmentsPort;
    }

    @Override
    public void translate(TranslateEbookCommand command) {
        epubFormatValidator.validate(command.filename(), command.content());
        var epubPath = extractEpubPort.unzipEpub(command.content());
        var contentOpfPath = epubPath.resolve(CONTENT_OPF_PATH);
        var parsedContentOpf = contentOpfParser.parse(contentOpfPath);
        var resolver = readerOrderResolver.resolve(parsedContentOpf.manifest(), parsedContentOpf.spine());
        List<DocumentTranslationPayload> documentsToTranslate =
                resolver.stream()
                        .map(readerOrderItem -> {
                            var document = htmlParser.parse(contentOpfPath.getParent().resolve(readerOrderItem.href()));
                            return htmlTranslationExtractorService.extract(readerOrderItem.href(), document);
                        })
                        .toList();

        var translateRequest = new TranslateSegmentsRequest("en", "fr", documentsToTranslate);
        var translatedDocuments = translateSegmentsPort.translate(translateRequest);
    }
}
