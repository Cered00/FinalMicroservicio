package com.Microservicio.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
public class Product {

    private Integer id;  
    private String name;
    private String description;
    private double price;
    private int stock;
}