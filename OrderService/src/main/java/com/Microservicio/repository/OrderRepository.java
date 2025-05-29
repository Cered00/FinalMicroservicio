package com.Microservicio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Microservicio.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    
    List<Order> findAll();

    Order save(Order pedido);
}