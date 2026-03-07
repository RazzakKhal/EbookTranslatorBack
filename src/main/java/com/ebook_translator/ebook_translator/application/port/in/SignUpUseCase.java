package com.ebook_translator.ebook_translator.application.port.in;

import com.ebook_translator.ebook_translator.application.dto.SignUpCommand;

public interface SignUpUseCase {

    void signUp(SignUpCommand signUpCommand);
}
