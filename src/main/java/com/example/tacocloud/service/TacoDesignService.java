package com.example.tacocloud.service;

import com.example.tacocloud.model.Ingredient;
import com.example.tacocloud.model.Taco;
import com.example.tacocloud.repository.IngredientRepository;
import com.example.tacocloud.repository.TacoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TacoDesignService {

    private final IngredientRepository ingredientRepo;
    private final TacoRepository tacoRepo;

    public TacoDesignService(IngredientRepository ingredientRepo, TacoRepository tacoRepo) {
        this.ingredientRepo = ingredientRepo;
        this.tacoRepo = tacoRepo;
    }

    public List<Ingredient> getAllIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredientRepo.findAll().forEach(ingredients::add);
        return ingredients;
    }

    public Map<String, List<Ingredient>> getIngredientsByType() {
        List<Ingredient> ingredients = getAllIngredients();
        Map<String, List<Ingredient>> map = new HashMap<>();
        for (Ingredient.Type type : Ingredient.Type.values()) {
            map.put(type.toString().toLowerCase(),
                    ingredients.stream()
                            .filter(i -> i.getType().equals(type))
                            .collect(Collectors.toList()));
        }
        return map;
    }

    public Taco saveDesign(Taco design) {
        return tacoRepo.save(design);
    }
}