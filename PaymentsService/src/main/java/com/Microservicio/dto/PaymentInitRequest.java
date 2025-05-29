package com.Microservicio.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data // Proporciona getters, setters, toString, etc. (requiere Lombok)
@NoArgsConstructor // Constructor sin argumentos
@AllArgsConstructor // Constructor con todos los argumentos
public class PaymentInitRequest {
    private int idPedido; // El ID de la orden
    private Double monto; // El monto del pago
}