package com.ebook_translator.ebook_translator.infrastructure.adapter.in.web.mapper;

import com.ebook_translator.ebook_translator.application.dto.TranslateEbookCommand;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class TranslateWebMapper {

    public TranslateEbookCommand toCommand(MultipartFile file) throws IOException {
        return new TranslateEbookCommand(file.getOriginalFilename(), file.getContentType(), file.getBytes());
    }
}
