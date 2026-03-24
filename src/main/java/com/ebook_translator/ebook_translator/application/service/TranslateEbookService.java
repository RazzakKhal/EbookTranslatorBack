package com.ebook_translator.ebook_translator.application.service;

import com.ebook_translator.ebook_translator.application.dto.TranslateEbookCommand;
import com.ebook_translator.ebook_translator.application.port.in.TranslateEbookUseCase;
import com.ebook_translator.ebook_translator.application.port.out.ExtractEpubPort;
import com.ebook_translator.ebook_translator.domain.service.EpubFormatValidator;
import com.ebook_translator.ebook_translator.domain.service.HtmlParser;
import com.ebook_translator.ebook_translator.domain.service.ReaderOrderResolver;
import com.ebook_translator.ebook_translator.infrastructure.adapter.out.storage.opf.ContentOpfParser;
import org.jsoup.nodes.Document;

import java.util.List;
import java.util.stream.Collectors;

public class TranslateEbookService implements TranslateEbookUseCase {

    private final EpubFormatValidator epubFormatValidator;
    private final ExtractEpubPort extractEpubPort;
    private final ContentOpfParser contentOpfParser;
    private final ReaderOrderResolver readerOrderResolver;
    private final HtmlParser htmlParser;

    private final String CONTENT_OPF_PATH = "content.opf";

    public TranslateEbookService(
            EpubFormatValidator epubFormatValidator,
            ExtractEpubPort extractEpubPort,
            ContentOpfParser contentOpfParser,
            ReaderOrderResolver readerOrderResolver,
            HtmlParser htmlParser
    ) {
        this.epubFormatValidator = epubFormatValidator;
        this.extractEpubPort = extractEpubPort;
        this.contentOpfParser = contentOpfParser;
        this.readerOrderResolver = readerOrderResolver;
        this.htmlParser = htmlParser;
    }

    @Override
    public void translate(TranslateEbookCommand command) {
        epubFormatValidator.validate(command.filename(), command.content());
        var epubPath = extractEpubPort.unzipEpub(command.content());
        var contentOpfPath = epubPath.resolve(CONTENT_OPF_PATH);
        var parsedContentOpf = contentOpfParser.parse(contentOpfPath);
        var resolver = readerOrderResolver.resolve(parsedContentOpf.manifest(), parsedContentOpf.spine());
        List<Document> htmlsToTranslate =
                resolver.stream()
                        .map((readerOrderItem) -> htmlParser.parse(contentOpfPath.getParent().resolve(readerOrderItem.href())))
                        .collect(Collectors.toList());

    }
}
