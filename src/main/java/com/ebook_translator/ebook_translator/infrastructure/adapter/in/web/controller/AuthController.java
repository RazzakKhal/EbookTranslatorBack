package com.ebook_translator.ebook_translator.infrastructure.adapter.in.web.controller;

import com.ebook_translator.ebook_translator.application.port.in.SignUpUseCase;
import com.ebook_translator.ebook_translator.application.service.SignUpService;
import com.ebook_translator.ebook_translator.infrastructure.adapter.in.web.dto.request.SignUpRequest;
import com.ebook_translator.ebook_translator.infrastructure.adapter.in.web.mapper.SignUpWebMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthController {

    private final SignUpUseCase signUpService;
    private final SignUpWebMapper signUpWebMapper;

    public AuthController(SignUpService signUpService, SignUpWebMapper signUpWebMapper) {
        this.signUpService = signUpService;
        this.signUpWebMapper = signUpWebMapper;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(
            @RequestBody SignUpRequest signUpRequest
    ) {
        
        signUpService.signUp(signUpWebMapper.toCommand(signUpRequest));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
