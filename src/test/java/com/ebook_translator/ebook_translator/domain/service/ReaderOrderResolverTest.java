package com.ebook_translator.ebook_translator.domain.service;

import com.ebook_translator.ebook_translator.domain.exception.InvalidEbookFormatException;
import com.ebook_translator.ebook_translator.domain.model.Manifest;
import com.ebook_translator.ebook_translator.domain.model.ManifestItem;
import com.ebook_translator.ebook_translator.domain.model.ReaderOrderItem;
import com.ebook_translator.ebook_translator.domain.model.Spine;
import com.ebook_translator.ebook_translator.domain.model.SpineItem;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReaderOrderResolverTest {

    private final ReaderOrderResolver resolver = new ReaderOrderResolver();

    @Test
    void resolve_shouldReturnReaderOrderFollowingSpineOrder() {
        Manifest manifest = new Manifest(List.of(
                new ManifestItem("chapter-1", "text/chapter-1.xhtml", "application/xhtml+xml"),
                new ManifestItem("chapter-2", "text/chapter-2.xhtml", "application/xhtml+xml"),
                new ManifestItem("chapter-3", "text/chapter-3.xhtml", "application/xhtml+xml")
        ));
        Spine spine = new Spine(List.of(
                new SpineItem("chapter-2"),
                new SpineItem("chapter-1")
        ));

        List<ReaderOrderItem> readerOrder = resolver.resolve(manifest, spine);

        assertEquals(2, readerOrder.size());
        assertEquals("chapter-2", readerOrder.get(0).idRef());
        assertEquals("text/chapter-2.xhtml", readerOrder.get(0).href());
        assertEquals("chapter-1", readerOrder.get(1).idRef());
        assertEquals("text/chapter-1.xhtml", readerOrder.get(1).href());
    }

    @Test
    void resolve_shouldThrowInvalidEbookFormatException_whenSpineReferencesUnknownManifestItem() {
        Manifest manifest = new Manifest(List.of(
                new ManifestItem("chapter-1", "text/chapter-1.xhtml", "application/xhtml+xml")
        ));
        Spine spine = new Spine(List.of(
                new SpineItem("chapter-1"),
                new SpineItem("chapter-2")
        ));

        assertThrows(InvalidEbookFormatException.class, () -> resolver.resolve(manifest, spine));
    }
}
