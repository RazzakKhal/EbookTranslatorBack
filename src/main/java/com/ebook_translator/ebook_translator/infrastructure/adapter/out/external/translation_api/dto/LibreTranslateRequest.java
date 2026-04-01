package com.ebook_translator.ebook_translator.infrastructure.adapter.out.external.translation_api.dto;

public record LibreTranslateRequest(
        String q,
        String source,
        String target,
        String format
) {
}
