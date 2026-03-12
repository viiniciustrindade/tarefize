package com.senai.todolist.domain.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    USUARIO_JA_EXISTE(HttpStatus.CONFLICT),
    USUARIO_NAO_ENCONTRADO(HttpStatus.NOT_FOUND),
    TAREFA_NAO_EXISTE(HttpStatus.NOT_FOUND),
    EMAIL_JA_CADASTRADO(HttpStatus.CONFLICT),
    CREDENCIAIS_INVALIDAS(HttpStatus.UNAUTHORIZED),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST),
    ERRO_COMUNICACAO_BROKER(HttpStatus.SERVICE_UNAVAILABLE),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR);

    private final HttpStatus httpStatus;

    ErrorCode(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
