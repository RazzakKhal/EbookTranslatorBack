package com.ebook_translator.ebook_translator.infrastructure.adapter.out.storage.opf.mapper;

import com.ebook_translator.ebook_translator.domain.exception.InvalidEbookFormatException;
import com.ebook_translator.ebook_translator.domain.model.Spine;
import com.ebook_translator.ebook_translator.infrastructure.adapter.out.storage.opf.dto.SpineItemRefXml;
import com.ebook_translator.ebook_translator.infrastructure.adapter.out.storage.opf.dto.SpineXml;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SpineXmlMapperTest {

    private final SpineXmlMapper mapper = new SpineXmlMapper();

    @Test
    void map_shouldReturnSpine_whenSpineXmlIsValid() throws ReflectiveOperationException {
        SpineItemRefXml firstItem = spineItem("chapter-1");
        SpineItemRefXml secondItem = spineItem("chapter-2");
        SpineXml spineXml = spineXml(List.of(firstItem, secondItem));

        Spine spine = mapper.map(spineXml);

        assertEquals(2, spine.items().size());
        assertEquals("chapter-1", spine.items().get(0).idRef());
        assertEquals("chapter-2", spine.items().get(1).idRef());
    }

    @Test
    void map_shouldThrowInvalidEbookFormatException_whenSpineXmlIsNull() {
        assertThrows(InvalidEbookFormatException.class, () -> mapper.map(null));
    }

    @Test
    void map_shouldThrowInvalidEbookFormatException_whenItemsAreNull() throws ReflectiveOperationException {
        SpineXml spineXml = new SpineXml();

        setField(spineXml, "items", null);

        assertThrows(InvalidEbookFormatException.class, () -> mapper.map(spineXml));
    }

    @Test
    void map_shouldThrowInvalidEbookFormatException_whenItemIdRefIsBlank() throws ReflectiveOperationException {
        SpineItemRefXml item = spineItem(" ");
        SpineXml spineXml = spineXml(List.of(item));

        assertThrows(InvalidEbookFormatException.class, () -> mapper.map(spineXml));
    }

    private static SpineXml spineXml(List<SpineItemRefXml> items) throws ReflectiveOperationException {
        SpineXml spineXml = new SpineXml();
        setField(spineXml, "items", items);
        return spineXml;
    }

    private static SpineItemRefXml spineItem(String idRef) throws ReflectiveOperationException {
        SpineItemRefXml item = new SpineItemRefXml();
        setField(item, "idRef", idRef);
        return item;
    }

    private static void setField(Object target, String fieldName, Object value) throws ReflectiveOperationException {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
