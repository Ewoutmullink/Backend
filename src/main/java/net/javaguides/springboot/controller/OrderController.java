package net.javaguides.springboot.controller;

import net.javaguides.springboot.entity.Order;
import net.javaguides.springboot.entity.Product;
import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.repository.OrderRepository;
import net.javaguides.springboot.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @PostMapping()
    public Order createOrder(@RequestBody List<Product> orderRequest) {
        Order order = new Order();
        this.orderRepository.save(order);
        orderRequest.forEach(product -> {
            order.addProduct(product);
        });
        return this.orderRepository.save(order);
    }

    @GetMapping("/{id}/products")
    public Set<Product> getProducts(@PathVariable Long id) {
        Optional<Order> order = orderRepository.findById(id);
        if(order.isPresent()) {
//            return order.get().getProductsOrder();
        }
        throw new ResourceNotFoundException("Order not found with id " + id);
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}