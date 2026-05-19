package com.senai.todolist.infraecstruture.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senai.todolist.domain.model.FailedEvent;
import com.senai.todolist.infraecstruture.repository.FailedEventRepository;
import com.senai.todolist.service.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationRetryJob {

    private final FailedEventRepository failedEventRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${app.kafka.topic.notification}")
    private String topic;

    @Scheduled(fixedDelay = 300000)
    @Transactional
    public void retryFailedEvents() {
        List<FailedEvent> failures = failedEventRepository.findByProcessedFalse();

        if (failures.isEmpty()) {
            return;
        }

        log.info("Iniciando reprocessamento de {} notificações que falharam anteriormente...", failures.size());

        for (FailedEvent failure : failures) {
            try {
                NotificationEvent envelope = objectMapper.readValue(
                        failure.getPayload(),
                        NotificationEvent.class
                );

                log.debug("Tentando reenviar envelope para: {} [Template: {}]",
                        envelope.destinatario(), envelope.template());

                kafkaTemplate.send(topic, envelope.destinatario(), envelope);


                failure.setProcessed(true);
                failedEventRepository.save(failure);

                log.info("Sucesso ao reprocessar evento ID: {} (Destinatário: {})",
                        failure.getId(), envelope.destinatario());

            } catch (Exception e) {
                log.error("Falha persistente no evento ID: {}. Erro: {}",
                        failure.getId(), e.getMessage());
            }
        }
    }
}