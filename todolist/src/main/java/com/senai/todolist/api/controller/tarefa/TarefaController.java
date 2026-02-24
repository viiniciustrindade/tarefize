package com.senai.todolist.api.controller.tarefa;

import com.senai.todolist.domain.dto.tarefa.TarefaPatchDto;
import com.senai.todolist.domain.dto.tarefa.TarefaRequisicaoDto;
import com.senai.todolist.domain.dto.tarefa.TarefaRespostaDto;
import com.senai.todolist.domain.model.Usuario;
import com.senai.todolist.service.tarefa.TarefaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/tarefas")
@RequiredArgsConstructor
public class TarefaController {
    private final TarefaService tarefaService;

    @PostMapping
    public ResponseEntity<TarefaRespostaDto> criarTarefa(
            @Valid @RequestBody TarefaRequisicaoDto requisicaoDto,
            @AuthenticationPrincipal Usuario usuario,
            UriComponentsBuilder uriBuilder
    ){
        TarefaRespostaDto resposta =
                tarefaService.criarTarefa(usuario,requisicaoDto);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(resposta.id())
                .toUri();

        return ResponseEntity.created(uri)
                .body(resposta);
    }

    @GetMapping
    public ResponseEntity<Page<TarefaRespostaDto>> buscarTodasAsTarefas(
            Pageable pageable,
            @AuthenticationPrincipal Usuario usuario
    ){
        Page<TarefaRespostaDto> pagina =
                tarefaService.buscarTodasAsTarefas(usuario,pageable);

            return ResponseEntity.ok(pagina);
    }

    @PatchMapping("/{idTarefa}")
    public ResponseEntity<TarefaRespostaDto> atualizaTarefa(
            @PathVariable Long idTarefa,
            @Valid@RequestBody TarefaPatchDto tarefaRequisicaoDto,
            @AuthenticationPrincipal Usuario usuario
    ){
        return ResponseEntity.ok(
                tarefaService.atualizarTarefa(
                        usuario,
                        idTarefa,
                        tarefaRequisicaoDto));
    }

    @DeleteMapping("/{idTarefa}")
    public ResponseEntity<Void> deletarTarefa(
            @PathVariable Long idTarefa,
            @AuthenticationPrincipal Usuario usuario
    ){
        tarefaService.deletarTarefaPorId(usuario,idTarefa);
        return ResponseEntity.noContent()
                .build();
    }

    @PatchMapping("/{idTarefa}/concluir/")
    public ResponseEntity<Void> concluirTarefa(
            @PathVariable Long idTarefa,
            @AuthenticationPrincipal Usuario usuario
    ){
        tarefaService.concluirTarefa(idTarefa,usuario);

        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }
}
