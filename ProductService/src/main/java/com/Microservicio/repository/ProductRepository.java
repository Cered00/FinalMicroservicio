package com.Microservicio.repository;


import com.Microservicio.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findAll();

    Product save(Product pedido);

    Optional<Product> findById(int idPedido);
}
