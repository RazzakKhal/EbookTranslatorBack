package com.ebook_translator.ebook_translator.domain.service;

import com.ebook_translator.ebook_translator.domain.exception.InvalidEbookFormatException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class EpubFormatValidator {

    public void validate(String filename, byte[] content) {
        if (filename == null || !filename.toLowerCase().endsWith(".epub")) {
            throw new InvalidEbookFormatException();
        }

        if (content == null || content.length == 0) {
            throw new InvalidEbookFormatException();
        }

        try (ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(content))) {
            ZipEntry firstEntry = zipInputStream.getNextEntry();

            if (firstEntry == null || !"mimetype".equals(firstEntry.getName())) {
                throw new InvalidEbookFormatException();
            }

            byte[] mimetypeBytes = zipInputStream.readAllBytes();
            String mimetype = new String(mimetypeBytes, StandardCharsets.UTF_8);

            if (!"application/epub+zip".equals(mimetype)) {
                throw new InvalidEbookFormatException();
            }

            boolean containerFound = false;
            ZipEntry entry;

            while ((entry = zipInputStream.getNextEntry()) != null) {
                if ("META-INF/container.xml".equals(entry.getName())) {
                    containerFound = true;
                    break;
                }
            }

            if (!containerFound) {
                throw new InvalidEbookFormatException();
            }

        } catch (IOException e) {
            throw new InvalidEbookFormatException();
        }
    }

}
