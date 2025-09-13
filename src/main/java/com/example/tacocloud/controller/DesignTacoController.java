package com.example.tacocloud.controller;

import com.example.tacocloud.model.Ingredient;
import com.example.tacocloud.model.Ingredient.Type;
import com.example.tacocloud.model.Order;
import com.example.tacocloud.model.Taco;
import com.example.tacocloud.repository.IngredientRepository;
import com.example.tacocloud.repository.TacoRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("order")
public class DesignTacoController {

    private final IngredientRepository ingredientRepo;
    private final TacoRepository designRepo;

    @Autowired
    public DesignTacoController(IngredientRepository ingredientRepo, TacoRepository designRepo) {
        this.ingredientRepo = ingredientRepo;
        this.designRepo = designRepo;
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
        List<Ingredient> ingredients = new ArrayList<>();
        ingredientRepo.findAll().forEach(i -> ingredients.add(i));
        Type[] types = Ingredient.Type.values();
        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(),
                    filterByType(ingredients, type));
        }
        model.addAttribute("design", new Taco());
        return "design";
    }

    private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
        return ingredients
                .stream()
                .filter(x -> x.getType().equals(type))
                .collect(Collectors.toList());
    }

    @PostMapping
    public String processDesign(
            @Valid @ModelAttribute("design") Taco design, Errors errors, @ModelAttribute Order order, Model model) {
        if (errors.hasErrors()) {
            List<Ingredient> ingredients = new ArrayList<>();
            ingredientRepo.findAll().forEach(ingredients::add);
            Type[] types = Ingredient.Type.values();
            for (Type type : types) {
                model.addAttribute(type.toString().toLowerCase(),
                        filterByType(ingredients, type));
            }
            return "design";
        }
        Taco saved = designRepo.save(design);
        order.addDesign(saved);
        log.info("Processing design: " + design);
        return "redirect:/orders/current";
    }
}
