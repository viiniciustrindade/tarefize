package com.senai.todolist.service.event;

import java.util.Map;

public record NotificationEvent(
        String destinatario,
        String tipoCanal,
        String template,
        Map<String, String> parametros
) {
}
