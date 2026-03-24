package com.ebook_translator.ebook_translator.domain.service;

import com.ebook_translator.ebook_translator.domain.exception.InvalidEbookFormatException;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HtmlParserTest {

    private final HtmlParser htmlParser = new HtmlParser();

    @Test
    void parse_shouldReturnDocument_whenHtmlFileExists() throws Exception {
        Path htmlPath = Files.createTempFile("chapter", ".xhtml");
        Files.writeString(htmlPath, """
                <!DOCTYPE html>
                <html>
                    <head>
                        <title>Chapter 1</title>
                    </head>
                    <body>
                        <p>Hello world</p>
                    </body>
                </html>
                """);

        try {
            Document document = htmlParser.parse(htmlPath);

            assertEquals("Chapter 1", document.title());
            assertEquals("Hello world", document.selectFirst("p").text());
        } finally {
            Files.deleteIfExists(htmlPath);
        }
    }

    @Test
    void parse_shouldThrowIOException_whenHtmlFileDoesNotExist() {
        Path missingHtmlPath = Path.of("missing-file.xhtml");

        assertThrows(InvalidEbookFormatException.class, () -> htmlParser.parse(missingHtmlPath));
    }
}
