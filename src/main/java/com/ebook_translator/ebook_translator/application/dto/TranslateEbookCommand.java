package com.ebook_translator.ebook_translator.application.dto;

public record TranslateEbookCommand(String filename, String type, byte[] content) {
}
