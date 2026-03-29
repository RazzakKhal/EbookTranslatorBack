package com.ebook_translator.ebook_translator.domain.service;

import com.ebook_translator.ebook_translator.domain.model.TranslationSegment;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HtmlTranslationExtractorServiceTest {

    private final HtmlTranslationExtractorService htmlTranslationExtractorService = new HtmlTranslationExtractorService();

    @Test
    void extract_shouldReturnDocumentTranslationPayloadWithOrderedSegments() {
        Document document = Jsoup.parse("""
                <!DOCTYPE html>
                <html>
                    <body>
                        <h2 class="title">Chapter 1</h2>
                        <p>Hello <em>world</em></p>
                        <ul>
                            <li>First item</li>
                            <li><p>Second item</p></li>
                        </ul>
                        <div>
                            <p>Nested paragraph</p>
                        </div>
                        <p>   </p>
                        <script>const ignored = true;</script>
                    </body>
                </html>
                """);

        var payload = htmlTranslationExtractorService.extract("Text/chapter-1.xhtml", document);

        assertEquals("Text/chapter-1.xhtml", payload.href());
        assertIterableEquals(
                List.of(
                        new TranslationSegment("segment-0", "Chapter 1"),
                        new TranslationSegment("segment-1", "Hello world"),
                        new TranslationSegment("segment-2", "First item"),
                        new TranslationSegment("segment-3", "Second item"),
                        new TranslationSegment("segment-4", "Nested paragraph")
                ),
                payload.segments()
        );
        assertEquals("segment-0", document.selectFirst("h2").attr(HtmlTranslationExtractorService.TRANSLATION_ID_ATTRIBUTE));
        assertEquals("segment-1", document.select("p").get(0).attr(HtmlTranslationExtractorService.TRANSLATION_ID_ATTRIBUTE));
        assertEquals("segment-2", document.select("li").get(0).attr(HtmlTranslationExtractorService.TRANSLATION_ID_ATTRIBUTE));
        assertTrue(document.select("li").get(1).attr(HtmlTranslationExtractorService.TRANSLATION_ID_ATTRIBUTE).isBlank());
    }
}
