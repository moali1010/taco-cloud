package com.example.tacocloud.controller;

import com.example.tacocloud.model.Order;
import com.example.tacocloud.service.OrderService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/current")
    public String orderForm(Model model) {
        if (!model.containsAttribute("order")) {
            model.addAttribute("order", new Order());
        }
        Order order = (Order) model.getAttribute("order");
        if (order != null) {
            log.info("Order form loaded with {} tacos",
                    order.getTacos() != null ? order.getTacos().size() : 0);
        }
        return "orderForm";
    }

    @PostMapping
    public String processOrder(@Valid Order order,
                               Errors errors,
                               SessionStatus sessionStatus,
                               Model model) {
        log.info("Processing order: {}", order);

        if (errors.hasErrors() || !orderService.isValid(order, errors)) {
            log.error("Order validation failed: {}", errors.getAllErrors());
            return "orderForm";
        }

        try {
            orderService.saveOrder(order);
            sessionStatus.setComplete();
            log.info("Order submitted successfully");
            return "redirect:/";
        } catch (Exception e) {
            log.error("Error saving order: {}", e.getMessage(), e);
            model.addAttribute("saveError", "There was an error saving your order. Please try again.");
            return "orderForm";
        }
    }
}