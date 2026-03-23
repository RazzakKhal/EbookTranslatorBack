package com.ebook_translator.ebook_translator.domain.model;

public record ParsedContentOpf(
        Manifest manifest,
        Spine spine
) {
}
