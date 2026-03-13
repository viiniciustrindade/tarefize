package com.senai.todolist.api.dto.tarefa;

import java.time.LocalDate;

public record TarefaRespostaDto(
        Long id,
        String nomeTarefa,
        String descricaoTarefa,
        int prioridade,
        LocalDate dataCricao
) {
}
