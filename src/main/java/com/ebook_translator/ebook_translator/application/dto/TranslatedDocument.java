package com.ebook_translator.ebook_translator.application.dto;

import java.util.List;

public record TranslatedDocument(String href, List<TranslatedSegment> segments) {
}
