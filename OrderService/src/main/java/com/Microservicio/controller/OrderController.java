package com.Microservicio.controller;

import java.time.LocalDateTime;
import java.util.List;

import com.Microservicio.dto.OrderStatusUpdateRequest;
import com.Microservicio.model.Order;
import com.Microservicio.model.ProductOrder;
import com.Microservicio.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<List<Order>> getAll() {
        List<Order> orders = orderService.findAll();
        if (!orders.isEmpty()) {
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping
    public ResponseEntity<Order> postPedido(@RequestBody Order pedido) {
        Order buscado = orderService.findById(pedido.getOrderID());
        if (buscado == null) {
            return new ResponseEntity<>(orderService.save(pedido), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping("/{idPedido}")
    public ResponseEntity<Order> getById(@PathVariable int idPedido) { // Cambiado a int
        Order pedido = orderService.findById(idPedido);
        return pedido != null ? new ResponseEntity<>(pedido, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create")
    public ResponseEntity<Order> crearPedido(@RequestParam String idCliente, @RequestBody List<ProductOrder> productos) {
        Order pedido = orderService.createOrder(idCliente, productos);
        if (pedido != null) {
            return new ResponseEntity<>(pedido, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PostMapping("/cancel/{idPedido}")
    public ResponseEntity<Order> cancelarPedido(@PathVariable int idPedido, @RequestBody String motivo) { // Cambiado a int
        Order pedido = orderService.cancelOrder(idPedido, motivo);
        if (pedido != null) {
            return new ResponseEntity<>(pedido, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/status/{idPedido}")
    public ResponseEntity<String> consultarEstado(@PathVariable int idPedido) { // Cambiado a int


        Order.Status estado = orderService.getStatus(idPedido);
        if (estado != Order.Status.NOT_FOUND) {
            return new ResponseEntity<>(estado.getValue(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

     @PostMapping("/update-status/{idPedido}")
    public ResponseEntity<Order> actualizarEstado(@PathVariable int idPedido, @RequestBody OrderStatusUpdateRequest request) {
        // AÑADIR ESTA LÍNEA PARA DEBUG
        System.out.println("DEBUG: Objeto request recibido: " + request);

        String nuevoEstadoString = request.getNuevoEstado(); 

        
        System.out.println("Recibiendo solicitud para actualizar estado de orden " + idPedido + " a: '" + nuevoEstadoString + "'");
        System.out.print("DEBUG (Controller): String recibido en binario (codigos): ");
        for (char c : nuevoEstadoString.toCharArray()) {
            System.out.print((int) c + " ");
        }
        System.out.println();
        String expectedCompletado = "Completado";
        System.out.print("DEBUG (Controller): String esperado 'Completado' (codigos): ");
        for (char c : expectedCompletado.toCharArray()) {
            System.out.print((int) c + " ");
        }
        System.out.println();


        Order.Status estado = Order.Status.fromValue(nuevoEstadoString); // Y fromValue con equalsIgnoreCase

        if (estado == null) {
            System.err.println("Estado inválido recibido: " + nuevoEstadoString);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Order pedido = orderService.updateStatus(idPedido, estado);
        if (pedido != null) {
            System.out.println("Orden " + idPedido + " actualizada a estado: " + estado.getValue());
            return new ResponseEntity<>(pedido, HttpStatus.OK);
        } else {
            System.err.println("Orden " + idPedido + " no encontrada para actualizar.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/count/{idProducto}")
    public ResponseEntity<Integer> contarProductosPedidos(@PathVariable int idProducto) { // Cambiado a int
        int cantidad = orderService.countProducts(idProducto);
        return new ResponseEntity<>(cantidad, HttpStatus.OK);
    }

}