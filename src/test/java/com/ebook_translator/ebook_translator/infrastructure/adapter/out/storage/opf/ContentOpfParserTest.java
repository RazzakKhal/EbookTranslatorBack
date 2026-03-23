package com.ebook_translator.ebook_translator.infrastructure.adapter.out.storage.opf;

import com.ebook_translator.ebook_translator.domain.exception.InvalidEbookFormatException;
import com.ebook_translator.ebook_translator.domain.model.ParsedContentOpf;
import com.ebook_translator.ebook_translator.infrastructure.adapter.out.storage.opf.mapper.ManifestXmlMapper;
import com.ebook_translator.ebook_translator.infrastructure.adapter.out.storage.opf.mapper.SpineXmlMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ContentOpfParserTest {

    private final ContentOpfParser parser = new ContentOpfParser(new ManifestXmlMapper(), new SpineXmlMapper());

    @Test
    void parse_shouldReturnParsedContentOpf_whenContentOpfIsValid() throws IOException {
        Path contentOpfPath = Files.createTempFile("content", ".opf");
        Files.writeString(contentOpfPath, """
                <?xml version="1.0" encoding="UTF-8"?>
                <package xmlns="http://www.idpf.org/2007/opf" version="2.0" unique-identifier="BookId">
                    <manifest>
                        <item id="chapter-1" href="text/chapter-1.xhtml" media-type="application/xhtml+xml"/>
                        <item id="style" href="styles/book.css" media-type="text/css"/>
                    </manifest>
                    <spine>
                        <itemref idref="chapter-1"/>
                    </spine>
                </package>
                """);

        try {
            ParsedContentOpf parsedContentOpf = parser.parse(contentOpfPath);

            assertEquals(2, parsedContentOpf.manifest().items().size());
            assertEquals("chapter-1", parsedContentOpf.manifest().items().get(0).id());
            assertEquals("text/chapter-1.xhtml", parsedContentOpf.manifest().items().get(0).href());
            assertEquals("application/xhtml+xml", parsedContentOpf.manifest().items().get(0).mediaType());
            assertEquals(1, parsedContentOpf.spine().items().size());
            assertEquals("chapter-1", parsedContentOpf.spine().items().get(0).idRef());
        } finally {
            Files.deleteIfExists(contentOpfPath);
        }
    }

    @Test
    void parse_shouldThrowInvalidEbookFormatException_whenXmlIsMalformed() throws IOException {
        Path contentOpfPath = Files.createTempFile("content-invalid", ".opf");
        Files.writeString(contentOpfPath, """
                <?xml version="1.0" encoding="UTF-8"?>
                <package xmlns="http://www.idpf.org/2007/opf">
                    <manifest>
                        <item id="chapter-1" href="text/chapter-1.xhtml" media-type="application/xhtml+xml">
                    </manifest>
                </package>
                """);

        try {
            assertThrows(InvalidEbookFormatException.class, () -> parser.parse(contentOpfPath));
        } finally {
            Files.deleteIfExists(contentOpfPath);
        }
    }
}
