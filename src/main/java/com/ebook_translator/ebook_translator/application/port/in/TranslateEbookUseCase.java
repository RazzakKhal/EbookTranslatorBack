package com.ebook_translator.ebook_translator.application.port.in;

import com.ebook_translator.ebook_translator.application.dto.TranslateEbookCommand;

public interface TranslateEbookUseCase {

    void translate(TranslateEbookCommand command);
}
