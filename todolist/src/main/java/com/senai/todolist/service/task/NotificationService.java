package com.senai.todolist.service.task;

import com.senai.todolist.service.event.NotificationEvent;

public interface NotificationService {
    void enviar(NotificationEvent evento);
}
