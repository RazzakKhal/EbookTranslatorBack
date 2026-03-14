package com.ebook_translator.ebook_translator.domain.exception;

import com.ebook_translator.ebook_translator.domain.enums.ErrorsMessages;

public class InvalidEbookFormatException extends RuntimeException {
    public InvalidEbookFormatException() {
        super(ErrorsMessages.INVALID_FILE_FORMAT.getCode());
    }
}
