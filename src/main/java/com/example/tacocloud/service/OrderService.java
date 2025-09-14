package com.example.tacocloud.service;

import com.example.tacocloud.model.Order;
import com.example.tacocloud.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

@Service
public class OrderService {

    private final OrderRepository orderRepo;

    public OrderService(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }

    public boolean isValid(Order order, Errors errors) {
        if (order.getTacos() == null || order.getTacos().isEmpty()) {
            errors.reject("tacos.required", "Your order must include at least one taco");
            return false;
        }
        return true;
    }

    public Order saveOrder(Order order) {
        return orderRepo.save(order);
    }
}