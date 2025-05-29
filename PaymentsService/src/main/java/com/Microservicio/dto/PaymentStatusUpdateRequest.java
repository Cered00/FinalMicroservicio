package com.Microservicio.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentStatusUpdateRequest {
    private String newStatus; // Aquí recibiremos el nuevo estado como un String
}