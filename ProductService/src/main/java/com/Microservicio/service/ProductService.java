package com.Microservicio.service;

import java.util.List;
import java.util.Optional;

import com.Microservicio.model.Product;
import com.Microservicio.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public Product findById(int idProduct) {
        Optional<Product> product = productRepository.findById(idProduct);
        return product.orElse(null);
    }

    public Product createProduct(String name, Double price, Integer stock) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setStock(stock);
        return productRepository.save(product);
    }

    public Product updateProduct(int idProduct, String name, Double price, Integer stock) {
        Product product = findById(idProduct);
        if (product != null) {
            product.setName(name);
            product.setPrice(price);
            product.setStock(stock);
            return productRepository.save(product);
        }
        return null;
    }

    public void deleteProduct(int idProduct) {
        productRepository.deleteById(idProduct);
    }
}