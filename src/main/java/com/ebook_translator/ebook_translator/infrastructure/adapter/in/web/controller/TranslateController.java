package com.ebook_translator.ebook_translator.infrastructure.adapter.in.web.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/translate")
public class TranslateController {

    @GetMapping("/test")
    public String test() {
        return "test réussi";
    }

    @GetMapping("")
    public String translate(Authentication authentication) {
        return "salut : " + authentication.toString();
    }

}
