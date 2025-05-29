package com.Microservicio.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
// import jakarta.persistence.JoinColumn; // Ya no necesitas JoinColumn para un campo simple
// import jakarta.persistence.ManyToOne; // Ya no necesitas ManyToOne
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pago")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id", unique = true) // <-- ¡CORREGIDO! Coincide con la DB (snake_case)
    private int id; // El nombre del campo Java puede seguir siendo 'id'

    @Column(name = "order_id", nullable = false) // <-- ¡CORREGIDO! Coincide con la DB (snake_case)
    private int orderId; // El nombre del campo Java puede seguir siendo 'orderId' 

    @Column(nullable = false)
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private Status status = Status.PENDING; 

    @Column(nullable = false)
    private LocalDateTime date;

    // Tu enum Status está bien
    public enum Status {
        NOT_FOUND("No encontrado"),
        PENDING("Pendiente"),
        APPROVED("Aprobado"),
        REJECTED("Rechazado");

        private final String status;

        Status(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }

        public static Status fromValue(String value) {
            for (Status s : Status.values()) {
                if (s.getStatus().equalsIgnoreCase(value)) {
                    return s;
                }
            }
            return null;
        }
    }
}