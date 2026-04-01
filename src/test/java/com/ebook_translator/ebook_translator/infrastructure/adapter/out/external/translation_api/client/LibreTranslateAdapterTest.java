package com.ebook_translator.ebook_translator.infrastructure.adapter.out.external.translation_api.client;

import com.ebook_translator.ebook_translator.application.dto.TranslateSegmentsRequest;
import com.ebook_translator.ebook_translator.application.dto.TranslateSegmentsResponse;
import com.ebook_translator.ebook_translator.domain.model.DocumentTranslationPayload;
import com.ebook_translator.ebook_translator.domain.model.TranslationSegment;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.test.web.client.MockRestServiceServer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class LibreTranslateAdapterTest {

    @Test
    void translate_shouldCallLibreTranslateForEachSegmentAndReturnTranslatedText() {
        RestClient.Builder restClientBuilder = RestClient.builder()
                .baseUrl("http://localhost:5000");
        MockRestServiceServer server = MockRestServiceServer.bindTo(restClientBuilder).build();
        LibreTranslateAdapter adapter = new LibreTranslateAdapter(restClientBuilder.build());

        server.expect(requestTo("http://localhost:5000/translate"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                          "q": "Hello world",
                          "source": "en",
                          "target": "fr",
                          "format": "text"
                        }
                        """))
                .andRespond(withSuccess("""
                        {
                          "translatedText": "Bonjour le monde"
                        }
                        """, MediaType.APPLICATION_JSON));

        TranslateSegmentsRequest request = new TranslateSegmentsRequest(
                "de",
                "it",
                List.of(new DocumentTranslationPayload(
                        "chapter-1.xhtml",
                        List.of(new TranslationSegment("segment-1", "Hello world"))
                ))
        );

        TranslateSegmentsResponse response = adapter.translate(request);

        assertEquals(1, response.documents().size());
        assertEquals("chapter-1.xhtml", response.documents().get(0).href());
        assertEquals(1, response.documents().get(0).segments().size());
        assertEquals("segment-1", response.documents().get(0).segments().get(0).id());
        assertEquals("Bonjour le monde", response.documents().get(0).segments().get(0).translatedText());
        server.verify();
    }
}
