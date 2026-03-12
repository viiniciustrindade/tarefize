package com.senai.todolist.service.tarefa;

import com.senai.todolist.service.event.NotificacaoEvento;

public interface NotificacaoService {
    void enviar(NotificacaoEvento evento);
}
