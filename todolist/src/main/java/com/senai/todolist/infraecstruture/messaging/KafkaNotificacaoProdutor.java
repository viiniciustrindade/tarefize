package com.senai.todolist.infraecstruture.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.senai.todolist.domain.exception.NotificationMessagingException;
import com.senai.todolist.domain.model.FailedEvent;
import com.senai.todolist.infraecstruture.repository.FailedEventRepository;
import com.senai.todolist.service.event.NotificacaoEvento;
import com.senai.todolist.service.tarefa.NotificacaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaNotificacaoProdutor implements NotificacaoService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final FailedEventRepository failedEventRepository;
    private final ObjectMapper objectMapper;

    @Value("${app.kafka.topic.notification}")
    private String topic;

    @Override
    public void enviar(NotificacaoEvento evento) {
        log.info("Despachando envelope de notificação para o tópico [{}]. Destinatário: {} | Canal: {} | Template: {}",
                topic, evento.destinatario(), evento.tipoCanal(), evento.template());

        try {
            kafkaTemplate.send(topic, evento.destinatario(), evento);

            log.info("Notificação enviada com sucesso para o broker. ID do Evento: {}", evento.template());
        } catch (Exception e) {
            log.error("Falha ao enviar notificação para {}. Iniciando salvamento em tabela de falhas.", evento.destinatario());
            handleFailure(evento, e);

            throw new NotificationMessagingException("Falha ao comunicar com Broker de Mensageria");
        }
    }

    private void handleFailure(NotificacaoEvento event, Exception e) {
        try {
            String jsonPayload = objectMapper.writeValueAsString(event);

            FailedEvent failure = new FailedEvent();
            failure.setEventType("NOTIFICATION_ENVELOPE_" + event.template());
            failure.setPayload(jsonPayload);
            failure.setErrorMessage(e.getMessage());
            failure.setProcessed(false);

            failedEventRepository.save(failure);

            log.info("Evento de notificação persistido no banco para reprocessamento posterior.");
        } catch (JsonProcessingException ex) {
            log.error("ERRO CRÍTICO: Não foi possível serializar o envelope para o banco de falhas!", ex);
        }
    }
}