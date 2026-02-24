package com.senai.todolist.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.w3c.dom.Text;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tarefa")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tarefa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_tarefa",nullable = false)
    private String nomeTarefa;

    @Column(name = "descricao_tarefa")
    private String descricaoTarefa;

    @Column(nullable = false)
    private int prioridade;

    @Column(name = "data_criacao" ,nullable = false)
    private LocalDate dataCriacao;

    @Column(nullable = false)
    private boolean concluida;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public Tarefa(String nomeTarefa, String descricaoTarefa, int prioridade){
        this.nomeTarefa = nomeTarefa;
        this.descricaoTarefa = descricaoTarefa;
        this.prioridade = prioridade;
        this.concluida = false;
        this.dataCriacao=LocalDate.now();
    }
}

