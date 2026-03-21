package com.ebook_translator.ebook_translator.infrastructure.adapter.out.storage;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EpubExtractionServiceTest {


    private static void deleteRecursively(Path root) throws IOException {
        if (root == null || Files.notExists(root)) {
            return;
        }

        try (var paths = Files.walk(root)) {
            paths.sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (RuntimeException e) {
            if (e.getCause() instanceof IOException ioException) {
                throw ioException;
            }
            throw e;
        }
    }

    @Test
    void unzipEpub_shouldExtractFixtureEpub() throws IOException {
        var service = new EpubExtractionService();
        Path extractedPath = null;

        try (InputStream inputStream = getClass().getResourceAsStream("/epub/epub_test.epub")) {
            assertNotNull(inputStream);

            extractedPath = service.unzipEpub(inputStream.readAllBytes());
            assertNotNull(extractedPath);
            assertTrue(Files.isDirectory(extractedPath));
            assertTrue(Files.exists(extractedPath.resolve("epub_test/mimetype")));
            assertTrue(Files.exists(extractedPath.resolve("epub_test/META-INF/container.xml")));
            assertTrue(Files.exists(extractedPath.resolve("epub_test/text/part0000.html")));
        } finally {
            deleteRecursively(extractedPath);
        }
    }
}
