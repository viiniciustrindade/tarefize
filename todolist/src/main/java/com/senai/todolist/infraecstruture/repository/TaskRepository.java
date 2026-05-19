package com.senai.todolist.infraecstruture.repository;

import com.senai.todolist.domain.model.Task;
import com.senai.todolist.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {
    Page<Task> findByUser(User user, Pageable pageable);
    Optional<Task> findByIdAndUser(Long id, User user);
    boolean existsByIdAndUser(Long id, User user);
    Page<Task> findAllByUserAndCompleted(User user, Boolean completed, Pageable pageable);
}
