package com.ebook_translator.ebook_translator.application.port.out;

import java.nio.file.Path;

public interface ExtractEpubPort {

    Path unzipEpub(byte[] content);
}
