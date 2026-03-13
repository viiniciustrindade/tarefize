package com.senai.todolist.domain.exception;

public class NotificationMessagingException extends BusinessException{
    public NotificationMessagingException(String message) {
        super(message, ErrorCode.ERRO_COMUNICACAO_BROKER);
    }
}
