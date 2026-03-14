package com.ebook_translator.ebook_translator.domain.enums;

import lombok.Getter;

@Getter
public enum ErrorsMessages {


    INVALID_FILE_FORMAT("invalid_file_format");

    private String code;

    ErrorsMessages(String code) {
        this.code = code;
    }

}
