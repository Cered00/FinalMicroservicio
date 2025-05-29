package com.Microservicio.repository;

import java.util.List;
import java.util.Optional;

import com.Microservicio.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    
    List<User> findAll();

    User save(User usuario);

    Optional<User> findByUsername(String username);
}