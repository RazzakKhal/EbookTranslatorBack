package com.ebook_translator.ebook_translator.infrastructure.adapter.out.storage;

import com.ebook_translator.ebook_translator.application.port.out.ExtractEpubPort;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class EpubExtractionService implements ExtractEpubPort {

    @Override
    public Path unzipEpub(byte[] content) {

        try (ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(content))) {

            Path tempDirectory = Files.createTempDirectory("ebook-" + UUID.randomUUID());
            ZipEntry entry;

            while ((entry = zipInputStream.getNextEntry()) != null) {

                var tempPath = tempDirectory.resolve(entry.getName()).normalize();

                if (entry.isDirectory()) {
                    Files.createDirectories(tempPath);
                } else {
                    var dir = Files.createDirectories(tempPath.getParent());
                    try (OutputStream outputStream = Files.newOutputStream(tempPath)) {
                        zipInputStream.transferTo(outputStream);
                    }
                }
            }
            zipInputStream.closeEntry();
            return tempDirectory;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
