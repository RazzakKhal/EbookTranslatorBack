package com.ebook_translator.ebook_translator.infrastructure.adapter.out.external.translation_api.client;

import com.ebook_translator.ebook_translator.application.dto.TranslateSegmentsRequest;
import com.ebook_translator.ebook_translator.application.dto.TranslateSegmentsResponse;
import com.ebook_translator.ebook_translator.application.dto.TranslatedDocument;
import com.ebook_translator.ebook_translator.application.dto.TranslatedSegment;
import com.ebook_translator.ebook_translator.application.port.out.TranslateSegmentsPort;
import com.ebook_translator.ebook_translator.infrastructure.adapter.out.external.translation_api.dto.LibreTranslateRequest;
import com.ebook_translator.ebook_translator.infrastructure.adapter.out.external.translation_api.dto.LibreTranslateResponse;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;

public class LibreTranslateAdapter implements TranslateSegmentsPort {

    private static final String LIBRE_TRANSLATE_BASE_URL = "http://localhost:5000";
    private static final String SOURCE_LANGUAGE = "en";
    private static final String TARGET_LANGUAGE = "fr";
    private static final String TRANSLATION_FORMAT = "text";

    private final RestClient restClient;

    public LibreTranslateAdapter() {
        this(RestClient.builder()
                .baseUrl(LIBRE_TRANSLATE_BASE_URL)
                .build());
    }

    LibreTranslateAdapter(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public TranslateSegmentsResponse translate(TranslateSegmentsRequest request) {
        List<TranslatedDocument> translatedDocuments = request.documents()
                .stream()
                .map(document -> new TranslatedDocument(
                        document.href(),
                        document.segments()
                                .stream()
                                .map(segment -> new TranslatedSegment(segment.id(), translateSegment(segment.text())))
                                .toList()
                ))
                .toList();

        var response = new TranslateSegmentsResponse(translatedDocuments);
        System.out.println(response);
        return response;
    }

    private String translateSegment(String text) {
        try {
            LibreTranslateResponse response = restClient.post()
                    .uri("/translate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new LibreTranslateRequest(text, SOURCE_LANGUAGE, TARGET_LANGUAGE, TRANSLATION_FORMAT))
                    .retrieve()
                    .body(LibreTranslateResponse.class);

            if (response == null || response.translatedText() == null) {
                throw new IllegalStateException("LibreTranslate returned an empty translation response");
            }

            return response.translatedText();
        } catch (RestClientException exception) {
            throw new IllegalStateException("Failed to translate segment with LibreTranslate", exception);
        }
    }
}
