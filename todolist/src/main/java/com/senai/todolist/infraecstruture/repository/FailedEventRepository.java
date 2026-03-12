package com.senai.todolist.infraecstruture.repository;

import com.senai.todolist.domain.model.FailedEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FailedEventRepository extends JpaRepository<FailedEvent, Long> {
    List<FailedEvent> findByProcessedFalse();
}
