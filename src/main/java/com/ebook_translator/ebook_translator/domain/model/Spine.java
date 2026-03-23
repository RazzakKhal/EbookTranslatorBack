package com.ebook_translator.ebook_translator.domain.model;

import java.util.List;

public record Spine(
        List<SpineItem> items
) {
}
