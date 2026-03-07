package com.ebook_translator.ebook_translator.application.service;

import com.ebook_translator.ebook_translator.application.dto.SignUpCommand;
import com.ebook_translator.ebook_translator.application.port.in.SignUpUseCase;
import com.ebook_translator.ebook_translator.application.port.out.CreateIdentityPort;
import org.springframework.stereotype.Service;

@Service
public class SignUpService implements SignUpUseCase {

    private final CreateIdentityPort createIdentityPort;

    public SignUpService(CreateIdentityPort createIdentityPort) {
        this.createIdentityPort = createIdentityPort;
    }

    @Override
    public void signUp(SignUpCommand command) {

        createIdentityPort.createIdentity(command.login(), command.password());

    }
}