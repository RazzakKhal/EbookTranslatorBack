package com.ebook_translator.ebook_translator.application.dto;

import com.ebook_translator.ebook_translator.domain.model.DocumentTranslationPayload;

import java.util.List;

public record TranslateSegmentsRequest(
        String sourceLanguage,
        String targetLanguage,
        List<DocumentTranslationPayload> documents
) {
}
