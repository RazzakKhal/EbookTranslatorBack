package com.ebook_translator.ebook_translator.infrastructure.adapter.in.web.controller;

import com.ebook_translator.ebook_translator.application.port.in.TranslateEbookUseCase;
import com.ebook_translator.ebook_translator.infrastructure.adapter.in.web.mapper.TranslateWebMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/translate")
public class TranslateController {

    private final TranslateEbookUseCase translateEbookUseCase;
    private final TranslateWebMapper translateWebMapper;

    public TranslateController(TranslateEbookUseCase translateEbookUseCase, TranslateWebMapper translateWebMapper) {
        this.translateEbookUseCase = translateEbookUseCase;
        this.translateWebMapper = translateWebMapper;
    }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Resource> translateEbook(
            @Valid @NotNull @RequestParam("ebook") MultipartFile ebook
    ) throws IOException {

        this.translateEbookUseCase.translate(translateWebMapper.toCommand(ebook));

        byte[] fakeContent = "fichier traduit fictif".getBytes(StandardCharsets.UTF_8);
        Resource resource = new ByteArrayResource(fakeContent);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"translated.txt\"")
                .contentLength(fakeContent.length)
                .body(resource);
    }

}
