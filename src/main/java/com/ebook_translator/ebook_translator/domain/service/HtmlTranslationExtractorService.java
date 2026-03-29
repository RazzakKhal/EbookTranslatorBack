package com.ebook_translator.ebook_translator.domain.service;

import com.ebook_translator.ebook_translator.domain.model.DocumentTranslationPayload;
import com.ebook_translator.ebook_translator.domain.model.TranslationSegment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class HtmlTranslationExtractorService {

    public static final String TRANSLATION_ID_ATTRIBUTE = "data-translation-id";

    private static final String TRANSLATABLE_ELEMENTS_QUERY =
            "h1,h2,h3,h4,h5,h6,p,li,blockquote,figcaption,caption,td,th,dt,dd";

    public DocumentTranslationPayload extract(String href, Document document) {
        if (document.body() == null) {
            return new DocumentTranslationPayload(href, List.of());
        }

        AtomicInteger segmentIndex = new AtomicInteger();

        List<TranslationSegment> segments = document.body()
                .select(TRANSLATABLE_ELEMENTS_QUERY)
                .stream()
                // Extract only the deepest matching blocks to avoid duplicate text segments.
                .filter(this::isLeafTranslatableElement)
                .filter(element -> !element.text().isBlank())
                .map(element -> toSegment(element, segmentIndex.getAndIncrement()))
                .toList();

        return new DocumentTranslationPayload(href, segments);
    }

    private boolean isLeafTranslatableElement(Element element) {
        return element.children().select(TRANSLATABLE_ELEMENTS_QUERY).isEmpty();
    }

    private TranslationSegment toSegment(Element element, int index) {
        String segmentId = "segment-" + index;
        element.attr(TRANSLATION_ID_ATTRIBUTE, segmentId);
        return new TranslationSegment(segmentId, element.text());
    }
}
