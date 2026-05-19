package com.senai.todolist.infraecstruture.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.senai.todolist.domain.exception.NotificationMessagingException;
import com.senai.todolist.domain.model.FailedEvent;
import com.senai.todolist.infraecstruture.repository.FailedEventRepository;
import com.senai.todolist.service.event.NotificationEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaNotificacaoProdutorTest {

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Mock
    private FailedEventRepository repository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private KafkaNotificationProdutor produtor;

    @Test
    @DisplayName("Deve salvar no banco de falhas e lançar exceção quando o Kafka falhar")
    void deveSalvarNoBancoQuandoKafkaFalhar() throws JsonProcessingException {
        var evento = new NotificationEvent(
                "vinicius@teste.com",
                "EMAIL",
                "TAREFA_CRIADA",
                Map.of("nome", "Teste Unitário")
        );

        when(kafkaTemplate.send(any(), any(), any()))
                .thenThrow(new RuntimeException("Kafka fora do ar"));

        when(objectMapper.writeValueAsString(any())).thenReturn("{\"json\":\"gerado\"}");

        assertThrows(NotificationMessagingException.class, () -> produtor.enviar(evento));

        verify(repository, times(1)).save(any(FailedEvent.class));

        verify(objectMapper, times(1)).writeValueAsString(evento);
    }

    @Test
    @DisplayName("Deve enviar com sucesso e não salvar no banco")
    void deveEnviarComSucesso() {
        var evento = new NotificationEvent(
                "vinicius@teste.com",
                "EMAIL",
                "TAREFA_CRIADA",
                Map.of("nome", "Sucesso")
        );

        produtor.enviar(evento);

        verify(kafkaTemplate, times(1)).send(any(), any(), eq(evento));
        verify(repository, never()).save(any());
    }
}