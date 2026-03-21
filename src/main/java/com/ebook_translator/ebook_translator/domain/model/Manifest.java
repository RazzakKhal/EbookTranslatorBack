package com.ebook_translator.ebook_translator.domain.model;

import java.util.List;

public record Manifest(
        List<ManifestItem> items
) {
}
