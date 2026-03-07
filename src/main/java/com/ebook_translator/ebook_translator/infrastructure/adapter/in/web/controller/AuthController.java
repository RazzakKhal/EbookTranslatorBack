package com.ebook_translator.ebook_translator.infrastructure.adapter.in.web.controller;

import com.ebook_translator.ebook_translator.application.service.SignUpService;
import com.ebook_translator.ebook_translator.infrastructure.adapter.in.web.dto.request.SignUpRequest;
import com.ebook_translator.ebook_translator.infrastructure.adapter.in.web.dto.response.SignUpResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthController {

    private final SignUpService signUpService;

    public AuthController(SignUpService signUpService) {
        this.signUpService = signUpService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResponse> signUp(
            @RequestBody SignUpRequest signUpRequest
    ) {
        var response = signUpService.signUp(signUpRequest);
        return ResponseEntity.ok(new SignUpResponse(""));
    }
}
