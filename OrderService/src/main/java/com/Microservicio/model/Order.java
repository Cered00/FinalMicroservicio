package com.Microservicio.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", unique = true)
    private int orderID;

    @Column(length = 50, nullable = false)
    private String clientID;

    @Column(nullable = false)
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
    private Status status = Status.PENDING; 

    @Column(length = 200)
    private String reason;

    @Column(nullable = false)
    private Double total;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true) 
    @JsonManagedReference
    private List<ProductOrder> productOrders = new ArrayList<>(); 
    public void setProductOrders(List<ProductOrder> productOrders) {
        this.productOrders.clear(); 
        if (productOrders != null) {
            for (ProductOrder item : productOrders) {
                this.productOrders.add(item);
                item.setOrder(this); 
            }
        }
    }

    public void addProductOrder(ProductOrder productOrder) {
        if (productOrder != null) {
            this.productOrders.add(productOrder);
            productOrder.setOrder(this);
        }
    }

    @AllArgsConstructor
    public enum Status {
        NOT_FOUND("No encontrado"),
        PENDING("Pendiente"),
        COMPLETED("Completado"),
        CANCELLED("Cancelado");

        private String value;

        public String getValue() {
            return value;
        }

        public static Status fromValue(String value) {
        System.out.println("DEBUG (fromValue): Intentando convertir: '" + value + "'");
        System.out.print("DEBUG (fromValue): Código de caracteres (recibido): ");
        for (char c : value.toCharArray()) {
        System.out.print((int) c + " ");
    }
    System.out.println();

    for (Status status : Status.values()) {
        String enumValue = status.getValue();
        System.out.println("DEBUG (fromValue): Comparando con enum: '" + enumValue + "'");
        System.out.print("DEBUG (fromValue): Código de caracteres (enum): ");
        for (char c : enumValue.toCharArray()) {
            System.out.print((int) c + " ");
        }
        System.out.println();

        if (enumValue.equalsIgnoreCase(value)) { // <<< Asegúrate de que siga siendo equalsIgnoreCase
            System.out.println("DEBUG (fromValue): ¡Coincidencia encontrada! " + status.name());
            return status;
        } else {
            System.out.println("DEBUG (fromValue): NO COINCIDE. '" + enumValue + "' vs '" + value + "'");
        }
    }
    System.err.println("DEBUG (fromValue): Falló - No se encontró un estado para el valor: '" + value + "'");
    return null;
        }
    }
}