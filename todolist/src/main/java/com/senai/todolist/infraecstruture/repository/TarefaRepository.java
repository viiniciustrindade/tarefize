package com.senai.todolist.infraecstruture.repository;

import com.senai.todolist.domain.model.Tarefa;
import com.senai.todolist.domain.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa,Long> {
    Page<Tarefa> findByUsuario(Usuario usuario, Pageable pageable);
    Optional<Tarefa> findByIdAndUsuario(Long id, Usuario usuario);
    boolean existsByIdAndUsuario(Long idTarefa, Usuario usuario);
    Page<Tarefa> findAllByUsuarioAndConcluida(Usuario usuario, Boolean concluida, Pageable pageable);
}
