package com.senai.todolist.domain.exception;

public class TaskNotFoundException extends BusinessException {
    public TaskNotFoundException(Long id) {
        super("A tarefa com id "+ id + "não existe.",ErrorCode.TAREFA_NAO_EXISTE);
    }
}
