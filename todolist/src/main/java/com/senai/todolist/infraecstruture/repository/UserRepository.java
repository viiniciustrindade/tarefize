package com.senai.todolist.infraecstruture.repository;

import com.senai.todolist.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByName(String name);

    Optional <User> findByEmail(String email);

    boolean existsByEmail(String email);
}
