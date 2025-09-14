package com.example.tacocloud.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Taco {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDateTime createdAt;
    @ManyToMany
    @JoinTable(
            name = "Taco_Ingredients",
            joinColumns = @JoinColumn(name = "taco_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    @Size(min = 1, message = "You must choose at least 1 ingredient")
    private List<Ingredient> ingredients = new ArrayList<>();
    @NotNull
    @Size(min = 5, message = "Name must be at least 5 characters long")
    private String name;

    @PrePersist
    void createdAt() {
        this.createdAt = LocalDateTime.now();
    }

    public void addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
    }

    public boolean hasIngredients() {
        return ingredients != null && !ingredients.isEmpty();
    }
}
