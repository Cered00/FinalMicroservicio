package com.Microservicio.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Microservicio.model.Order;
import com.Microservicio.model.ProductOrder;
import com.Microservicio.repository.OrderRepository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestTemplate restTemplate;

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order save(Order pedido) {
        return orderRepository.save(pedido);
    }

    public Order findById(int idPedido) {
        Optional<Order> pedido = orderRepository.findById(idPedido);
        return pedido.orElse(null);
    }

    public Order createOrder(String idCliente, List<ProductOrder> productos) {
        Double total = productos.stream().mapToDouble(ProductOrder::getSubtotal).sum();
        Order pedido = new Order();
        pedido.setClientID(idCliente);
        pedido.setDate(LocalDateTime.now());
        pedido.setStatus(Order.Status.PENDING);
        pedido.setTotal(total);
        pedido.setProductOrders(productos);

/*        for (ProductOrder pp : productos) {
            pp.setOrder(pedido);
            String url = "http://localhost:8081/products/" + pp.getProduct().getId();
            Product product = restTemplate.getForObject(url, Product.class);
            if (product != null) {
                pp.setProduct(product);
            }
        }*/

        return orderRepository.save(pedido);
    }

    public Order cancelOrder(int idPedido, String motivo) { // Cambiado a int
        Optional<Order> pedidoOpt = orderRepository.findById(idPedido);
        if (pedidoOpt.isPresent()) {
            Order pedido = pedidoOpt.get();
            pedido.setStatus(Order.Status.CANCELLED);
            pedido.setReason(motivo);
            return orderRepository.save(pedido);
        }
        return null;
    }

    public Order.Status getStatus(int idPedido) { // Cambiado a int
        Optional<Order> pedidoOpt = orderRepository.findById(idPedido);
        return pedidoOpt.map(Order::getStatus).orElse(Order.Status.NOT_FOUND);
    }

    public Order updateStatus(int idPedido, Order.Status nuevoEstado) { // Cambiado a int
        Optional<Order> pedidoOpt = orderRepository.findById(idPedido);
        if (pedidoOpt.isPresent()) {
            Order pedido = pedidoOpt.get();
            pedido.setStatus(nuevoEstado);
            return orderRepository.save(pedido);
        }
        return null;
    }


    public int countProducts(int idProducto) { // Cambiado a int
        String url = "http://localhost:8081/products/" + idProducto + "/count";
        Integer count = restTemplate.getForObject(url, Integer.class);
        return count != null ? count : 0;
    }
}