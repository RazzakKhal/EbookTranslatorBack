package com.ebook_translator.ebook_translator.application.port.out;

import com.ebook_translator.ebook_translator.application.dto.TranslateSegmentsRequest;
import com.ebook_translator.ebook_translator.application.dto.TranslateSegmentsResponse;

public interface TranslateSegmentsPort {

    TranslateSegmentsResponse translate(TranslateSegmentsRequest request);
}
