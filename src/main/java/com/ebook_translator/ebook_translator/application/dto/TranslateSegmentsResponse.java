package com.ebook_translator.ebook_translator.application.dto;

import java.util.List;

public record TranslateSegmentsResponse(List<TranslatedDocument> documents) {
}
