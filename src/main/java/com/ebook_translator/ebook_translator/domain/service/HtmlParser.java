package com.ebook_translator.ebook_translator.domain.service;

import com.ebook_translator.ebook_translator.domain.exception.InvalidEbookFormatException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.nio.file.Path;

public class HtmlParser {

    public HtmlParser() {
    }

    public Document parse(Path htmlPath) {
        try {
            return Jsoup.parse(htmlPath);

        } catch (Exception e) {
            throw new InvalidEbookFormatException();
        }
    }
}
