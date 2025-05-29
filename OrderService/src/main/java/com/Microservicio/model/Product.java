package com.Microservicio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productId", unique = true)
    private int id; // Cambiado a int

    @Column(length = 50, nullable = false)
    private String name;

    @Column(nullable = false)
    private Double price;

    @Column( nullable = false)
    private Integer stock;
}