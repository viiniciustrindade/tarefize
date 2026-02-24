package com.senai.todolist.service.tarefa;

import com.senai.todolist.domain.dto.tarefa.TarefaPatchDto;
import com.senai.todolist.domain.dto.tarefa.TarefaRequisicaoDto;
import com.senai.todolist.domain.dto.tarefa.TarefaRespostaDto;
import com.senai.todolist.domain.exception.TarefaNãoExisteException;
import com.senai.todolist.domain.mapper.TarefaMapper;
import com.senai.todolist.domain.model.Tarefa;
import com.senai.todolist.domain.model.Usuario;
import com.senai.todolist.infraecstruture.repository.TarefaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TarefaService {
    private final TarefaRepository tarefaRepository;

    private final TarefaMapper tarefaMapper;

    public TarefaRespostaDto criarTarefa(Usuario usuario,
                                         TarefaRequisicaoDto tarefaRequisicaoDto){
        Tarefa tarefa = tarefaMapper.toEntity(tarefaRequisicaoDto);
        tarefa.setUsuario(usuario);

        tarefaRepository.save(tarefa);
        return tarefaMapper.toRespostaDto(tarefa);
    }

    public Page<TarefaRespostaDto> buscarTodasAsTarefas(
            Usuario usuario,Pageable pageable) {

        Page<Tarefa> pagina = tarefaRepository.findByUsuario(usuario,pageable);

        return pagina.map(tarefaMapper::toRespostaDto);
    }

    public TarefaRespostaDto atualizarTarefa(
            Usuario usuario, Long idTarefa,
            TarefaPatchDto patchDto) {

        Tarefa tarefa = tarefaRepository.findByIdAndUsuario(idTarefa, usuario)
                .orElseThrow(() -> new TarefaNãoExisteException(idTarefa));

        if (patchDto.nomeTarefa() != null) {
            tarefa.setNomeTarefa(patchDto.nomeTarefa());
        }
        if (patchDto.descricaoTarefa() != null) {
            tarefa.setDescricaoTarefa(patchDto.descricaoTarefa());
        }
        if (patchDto.prioridade() != null) {
            tarefa.setPrioridade(patchDto.prioridade());
        }

        tarefaRepository.save(tarefa);
        return tarefaMapper.toRespostaDto(tarefa);
    }

    public void deletarTarefaPorId(Usuario usuario, Long idTarefa) {
        if(!tarefaRepository.existsByIdAndUsuario(idTarefa,usuario)){
            throw new TarefaNãoExisteException(idTarefa);
        }

        tarefaRepository.deleteById(idTarefa);
    }

    public void concluirTarefa(
            Long idTarefa, Usuario usuario){
        Tarefa tarefa = tarefaRepository.findByIdAndUsuario(idTarefa, usuario)
                .orElseThrow(() -> new TarefaNãoExisteException(idTarefa));

        tarefa.setConcluida(true);
        tarefaRepository.save(tarefa);
    }
}
