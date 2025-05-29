package com.Microservicio.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusUpdateRequest {
    private String nuevoEstado; // ¡Este nombre de variable es CRÍTICO!
}