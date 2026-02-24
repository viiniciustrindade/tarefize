package com.senai.todolist.domain.mapper;

import com.senai.todolist.domain.dto.tarefa.TarefaRequisicaoDto;
import com.senai.todolist.domain.dto.tarefa.TarefaRespostaDto;
import com.senai.todolist.domain.model.Tarefa;
import org.springframework.stereotype.Component;

@Component
public class TarefaMapper {
    public Tarefa toEntity(TarefaRequisicaoDto requisicaoDto) {
        return new Tarefa(requisicaoDto.nomeTarefa(), requisicaoDto.descricaoTarefa(), requisicaoDto.prioridade());
    }

    public TarefaRespostaDto toRespostaDto(Tarefa tarefa) {
        return new TarefaRespostaDto(tarefa.getId(),
                tarefa.getNomeTarefa(),
                tarefa.getDescricaoTarefa(),
                tarefa.getPrioridade(),
                tarefa.getDataCriacao());
    }
}
