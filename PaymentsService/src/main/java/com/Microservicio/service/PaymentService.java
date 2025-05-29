package com.Microservicio.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map; // Importa Map
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Microservicio.model.Payment;
import com.Microservicio.repository.PaymentRepository;
import org.springframework.web.client.HttpClientErrorException; // Para capturar errores HTTP específicos
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private RestTemplate restTemplate; // Asegúrate de tener un bean RestTemplate configurado en tu @Configuration

    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    public Payment save(Payment pago) {
        return paymentRepository.save(pago);
    }

    public Payment findById(int idPago) {
        Optional<Payment> pago = paymentRepository.findById(idPago);
        return pago.orElse(null);
    }

    public Payment initPayment(int idPedido, Double monto) {
        // URL del OrderService. Asegúrate que el puerto y la ruta sean correctos.
        // Si tu OrderService está en 8080 y su endpoint para obtener órdenes por ID es /api/v1/orders/{id}
        String url = "http://localhost:8080/api/v1/orders/" + idPedido; // ¡Ajusta este puerto si es diferente!

        Map<String, Object> orderData = null; // Cambiamos Order.class a Map.class o LinkedHashMap.class
        try {
            orderData = restTemplate.getForObject(url, Map.class); // <-- CAMBIO CLAVE AQUÍ
        } catch (HttpClientErrorException.NotFound e) {
            // Captura específicamente el 404 Not Found si la orden no existe
            System.err.println("Orden con ID " + idPedido + " no encontrada en OrderService.");
            return null;
        } catch (Exception e) {
            // Otros errores de comunicación (ej. servicio caído, 5xx, etc.)
            System.err.println("Error al obtener detalles de la orden " + idPedido + " desde OrderService: " + e.getMessage());
            return null;
        }

        if (orderData != null && orderData.containsKey("orderID")) { // Verifica que el mapa no es nulo y contiene la clave
            // Extrae el orderID del mapa. ¡CUIDADO CON EL TIPO!
            // Jackson puede deserializar números a Integer o Long dependiendo del valor.
            Integer fetchedOrderId = (Integer) orderData.get("orderID"); // <-- EXTRAE EL ID MANUALMENTE
            
            if (fetchedOrderId != null && fetchedOrderId > 0) {
                Payment pago = new Payment();
                pago.setOrderId(fetchedOrderId); // Asigna el ID obtenido del mapa
                pago.setAmount(monto);
                pago.setStatus(Payment.Status.PENDING);
                pago.setDate(LocalDateTime.now());
                return paymentRepository.save(pago);
            }
        }
        System.err.println("No se pudo obtener un orderID válido de la respuesta del OrderService para la orden " + idPedido + ".");
        return null;
    }

    public Payment processPayment(int idPago) {
        Optional<Payment> pagoOpt = paymentRepository.findById(idPago);
        if (pagoOpt.isPresent()) {
            Payment pago = pagoOpt.get();
            if (pago.getStatus() == Payment.Status.PENDING) {
                pago.setStatus(Payment.Status.APPROVED);
                return paymentRepository.save(pago);
            } else {
                System.out.println("El pago " + idPago + " no está en estado PENDING, no se puede procesar.");
                return pago;
            }
        }
        return null;
    }

    public Payment updatePaymentStatus(int paymentId, String newStatusString) {
        Optional<Payment> paymentOptional = paymentRepository.findById(paymentId);
        if (paymentOptional.isPresent()) {
            Payment payment = paymentOptional.get();
            try {
                Payment.Status newStatus = Payment.Status.valueOf(newStatusString.toUpperCase());
                payment.setStatus(newStatus); 
                return paymentRepository.save(payment); 
            } catch (IllegalArgumentException e) {
                System.err.println("Estado de pago inválido proporcionado: '" + newStatusString + "'. Error: " + e.getMessage());
                return null; 
            }
        }
        return null; 
    }

    public Payment.Status getStatus(int idPago) {
        Optional<Payment> pagoOpt = paymentRepository.findById(idPago);
        return pagoOpt.map(Payment::getStatus).orElse(Payment.Status.NOT_FOUND);
    }
}