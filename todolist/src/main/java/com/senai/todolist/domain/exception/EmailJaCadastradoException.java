package com.senai.todolist.domain.exception;

public class EmailJaCadastradoException extends BusinessException {
    public EmailJaCadastradoException() {
        super("Já existe uma conta vinculada a este endereço de e-mail.",
                ErrorCode.EMAIL_JA_CADASTRADO);
    }
}
