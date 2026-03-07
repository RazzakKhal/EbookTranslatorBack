package com.ebook_translator.ebook_translator.infrastructure.adapter.in.web.mapper;

import com.ebook_translator.ebook_translator.application.dto.SignUpCommand;
import com.ebook_translator.ebook_translator.infrastructure.adapter.in.web.dto.request.SignUpRequest;
import org.springframework.stereotype.Component;

@Component
public class SignUpWebMapper {

    public SignUpCommand toCommand(SignUpRequest request) {
        return new SignUpCommand(request.login(), request.password());
    }


}
