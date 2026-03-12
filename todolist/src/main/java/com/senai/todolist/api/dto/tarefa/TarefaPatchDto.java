package com.senai.todolist.api.dto.tarefa;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TarefaPatchDto(
        String nomeTarefa,
        String descricaoTarefa,
        Integer prioridade
) {
}
