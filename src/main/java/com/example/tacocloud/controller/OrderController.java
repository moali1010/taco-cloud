package com.example.tacocloud.controller;

import com.example.tacocloud.model.Order;
import com.example.tacocloud.repository.OrderRepository;
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

    private final OrderRepository orderRepo;

    public OrderController(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }

    @GetMapping("/current")
    public String orderForm(Model model) {
        if (!model.containsAttribute("order")) {
            model.addAttribute("order", new Order());
        }
        Order order = (Order) model.getAttribute("order");
        if (order != null) {
            log.info("Order form loaded with " + (order.getTacos() != null ? order.getTacos().size() : 0) + " tacos");
        }
        return "orderForm";
    }

    @PostMapping
    public String processOrder(@Valid Order order, Errors errors, SessionStatus sessionStatus, Model model) {
        log.info("Processing order: " + order);
        if (errors.hasErrors()) {
            log.error("Order validation errors: " + errors.getAllErrors());
            return "orderForm";
        }
        if (order.getTacos() == null || order.getTacos().isEmpty()) {
            log.error("Order has no tacos");
            errors.reject("tacos.required", "Your order must include at least one taco");
            return "orderForm";
        }
        try {
            orderRepo.save(order);
            sessionStatus.setComplete(); // پاک کردن session attribute
            log.info("Order submitted: " + order);
            return "redirect:/";
        } catch (Exception e) {
            log.error("Error saving order: " + e.getMessage(), e);
            model.addAttribute("saveError", "There was an error saving your order. Please try again.");
            return "orderForm";
        }
    }
}
