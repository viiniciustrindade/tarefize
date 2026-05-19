package com.senai.todolist.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "task")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_tarefa",nullable = false)
    private String taskName;

    @Column(name = "descricao_tarefa")
    private String taskDescription;

    @Column(nullable = false)
    private int priority;

    @Column(name = "creation_date" ,nullable = false)
    private LocalDate creationDate;

    @Column(nullable = false)
    private boolean completed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Task(String taskName, String taskDescription, int priority){
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.priority = priority;
        this.completed = false;
        this.creationDate=LocalDate.now();
    }
}

