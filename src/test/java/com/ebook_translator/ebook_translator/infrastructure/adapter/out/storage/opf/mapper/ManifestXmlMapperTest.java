package com.ebook_translator.ebook_translator.infrastructure.adapter.out.storage.opf.mapper;

import com.ebook_translator.ebook_translator.domain.exception.InvalidEbookFormatException;
import com.ebook_translator.ebook_translator.domain.model.Manifest;
import com.ebook_translator.ebook_translator.infrastructure.adapter.out.storage.opf.dto.ManifestItemXml;
import com.ebook_translator.ebook_translator.infrastructure.adapter.out.storage.opf.dto.ManifestXml;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ManifestXmlMapperTest {

    private final ManifestXmlMapper mapper = new ManifestXmlMapper();

    @Test
    void map_shouldReturnManifest_whenManifestXmlIsValid() throws ReflectiveOperationException {
        ManifestItemXml firstItem = manifestItem("chapter-1", "text/chapter-1.xhtml", "application/xhtml+xml");
        ManifestItemXml secondItem = manifestItem("style", "styles/book.css", "text/css");
        ManifestXml manifestXml = manifestXml(List.of(firstItem, secondItem));

        Manifest manifest = mapper.map(manifestXml);

        assertEquals(2, manifest.items().size());
        assertEquals("chapter-1", manifest.items().get(0).id());
        assertEquals("text/chapter-1.xhtml", manifest.items().get(0).href());
        assertEquals("application/xhtml+xml", manifest.items().get(0).mediaType());
        assertEquals("style", manifest.items().get(1).id());
    }

    @Test
    void map_shouldThrowInvalidEbookFormatException_whenManifestXmlIsNull() {
        assertThrows(InvalidEbookFormatException.class, () -> mapper.map(null));
    }

    @Test
    void map_shouldThrowInvalidEbookFormatException_whenItemsAreNull() throws ReflectiveOperationException {
        ManifestXml manifestXml = new ManifestXml();

        setField(manifestXml, "items", null);

        assertThrows(InvalidEbookFormatException.class, () -> mapper.map(manifestXml));
    }

    @Test
    void map_shouldThrowInvalidEbookFormatException_whenItemIdIsBlank() throws ReflectiveOperationException {
        ManifestItemXml item = manifestItem(" ", "text/chapter-1.xhtml", "application/xhtml+xml");
        ManifestXml manifestXml = manifestXml(List.of(item));

        assertThrows(InvalidEbookFormatException.class, () -> mapper.map(manifestXml));
    }

    private static ManifestXml manifestXml(List<ManifestItemXml> items) throws ReflectiveOperationException {
        ManifestXml manifestXml = new ManifestXml();
        setField(manifestXml, "items", items);
        return manifestXml;
    }

    private static ManifestItemXml manifestItem(String id, String href, String mediaType) throws ReflectiveOperationException {
        ManifestItemXml item = new ManifestItemXml();
        setField(item, "id", id);
        setField(item, "href", href);
        setField(item, "mediaType", mediaType);
        return item;
    }

    private static void setField(Object target, String fieldName, Object value) throws ReflectiveOperationException {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
