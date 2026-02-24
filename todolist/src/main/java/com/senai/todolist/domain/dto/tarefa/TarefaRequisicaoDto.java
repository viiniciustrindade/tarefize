package com.senai.todolist.domain.dto.tarefa;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record TarefaRequisicaoDto(
        @NotBlank(message = "O nome da tarefa não pode estar em branco")
        @Size(max = 100, message = "O nome da tarefa deve ter no máximo 100 caracteres")
        String nomeTarefa,

        @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres")
        String descricaoTarefa,

        @Min(value = 1, message = "A prioridade mínima é 1")
        @Max(value = 5, message = "A prioridade máxima é 5")
        int prioridade
) {
}
