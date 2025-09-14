package com.example.tacocloud.controller;

import com.example.tacocloud.model.Ingredient;
import com.example.tacocloud.model.Order;
import com.example.tacocloud.model.Taco;
import com.example.tacocloud.service.TacoDesignService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/design")
@SessionAttributes("order")
@Slf4j
public class DesignTacoController {

    private final TacoDesignService designService;

    public DesignTacoController(TacoDesignService designService) {
        this.designService = designService;
    }

    @ModelAttribute(name = "order")
    public Order order() {
        return new Order();
    }

    @ModelAttribute(name = "taco")
    public Taco taco() {
        return new Taco();
    }

    @GetMapping
    public String showDesignForm(Model model) {
        Map<String, List<Ingredient>> ingredientsByType = designService.getIngredientsByType();
        ingredientsByType.forEach(model::addAttribute);
        model.addAttribute("design", new Taco());
        return "design";
    }

    @PostMapping
    public String processDesign(@Valid @ModelAttribute("design") Taco design,
                                Errors errors,
                                @ModelAttribute Order order,
                                Model model) {
        if (errors.hasErrors() || design.getIngredients() == null || design.getIngredients().isEmpty()) {
            if (design.getIngredients() == null || design.getIngredients().isEmpty()) {
                errors.rejectValue("ingredients", "ingredients.required", "You must choose at least 1 ingredient");
            }
            Map<String, List<Ingredient>> ingredientsByType = designService.getIngredientsByType();
            ingredientsByType.forEach(model::addAttribute);
            return "design";
        }

        try {
            Taco saved = designService.saveDesign(design);
            order.addDesign(saved);
            return "redirect:/orders/current";
        } catch (Exception e) {
            log.error("Error saving taco", e);
            model.addAttribute("saveError", "There was an error saving your taco. Please try again.");
            Map<String, List<Ingredient>> ingredientsByType = designService.getIngredientsByType();
            ingredientsByType.forEach(model::addAttribute);
            return "design";
        }
    }
}