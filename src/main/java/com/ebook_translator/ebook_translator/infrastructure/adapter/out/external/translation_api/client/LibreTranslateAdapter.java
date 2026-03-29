package com.ebook_translator.ebook_translator.infrastructure.adapter.out.external.translation_api.client;

import com.ebook_translator.ebook_translator.application.dto.TranslateSegmentsRequest;
import com.ebook_translator.ebook_translator.application.dto.TranslateSegmentsResponse;
import com.ebook_translator.ebook_translator.application.dto.TranslatedDocument;
import com.ebook_translator.ebook_translator.application.dto.TranslatedSegment;
import com.ebook_translator.ebook_translator.application.port.out.TranslateSegmentsPort;

import java.util.List;

public class LibreTranslateAdapter implements TranslateSegmentsPort {

    @Override
    public TranslateSegmentsResponse translate(TranslateSegmentsRequest request) {
        List<TranslatedDocument> translatedDocuments = request.documents()
                .stream()
                // traitement via call api
                .map(document -> new TranslatedDocument(
                        document.href(),
                        document.segments()
                                .stream()
                                .map(segment -> new TranslatedSegment(segment.id(), segment.text()))
                                .toList()
                ))
                .toList();

        return new TranslateSegmentsResponse(translatedDocuments);
    }
}
