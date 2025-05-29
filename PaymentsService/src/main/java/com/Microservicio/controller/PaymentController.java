package com.Microservicio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.Microservicio.dto.PaymentInitRequest; // Importa el DTO
import com.Microservicio.dto.PaymentStatusUpdateRequest;
import com.Microservicio.model.Payment;
import com.Microservicio.service.PaymentService; // Importa el servicio
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {
    @Autowired
    private PaymentService pagoService; 

    @GetMapping
    public ResponseEntity<List<Payment>> getAll() {
        List<Payment> pagos = pagoService.findAll();
        if (!pagos.isEmpty()) {
            return new ResponseEntity<>(pagos, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping 
    public ResponseEntity<Payment> postPago(@RequestBody Payment pago) {
        Payment buscado = pagoService.findById(pago.getId());
        if (buscado == null) {
            return new ResponseEntity<>(pagoService.save(pago), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping("/{idPago}")
    public ResponseEntity<Payment> getById(@PathVariable int idPago) {
        Payment pago = pagoService.findById(idPago);
        return pago != null ? new ResponseEntity<>(pago, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/start") 
    public ResponseEntity<Payment> initPayment(@RequestBody PaymentInitRequest request) {
        Payment newPayment = pagoService.initPayment(request.getIdPedido(), request.getMonto()); 
        if (newPayment != null) {
            System.out.println("Pago iniciado: " + newPayment);
            return new ResponseEntity<>(newPayment, HttpStatus.CREATED);
        } else {
            System.out.println("Pago no iniciado (posiblemente Order ID no encontrado)"); 
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
        }
    }

    @PostMapping("/process/{idPago}")
    public ResponseEntity<Payment> procesarPago(@PathVariable int idPago) {
        Payment pago = pagoService.processPayment(idPago); 
        if (pago != null) {
            return new ResponseEntity<>(pago, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/status/{idPago}")
    public ResponseEntity<String> consultarEstado(@PathVariable int idPago) {
        Payment.Status estado = pagoService.getStatus(idPago); 
        if (estado != Payment.Status.NOT_FOUND) {
            return new ResponseEntity<>(estado.getStatus(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/update-status/{idPago}")
    public ResponseEntity<Payment> updatePaymentStatus(@PathVariable int idPago, @RequestBody PaymentStatusUpdateRequest request) {
        Payment updatedPayment = pagoService.updatePaymentStatus(idPago, request.getNewStatus());

        if (updatedPayment != null) {
            return new ResponseEntity<>(updatedPayment, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}