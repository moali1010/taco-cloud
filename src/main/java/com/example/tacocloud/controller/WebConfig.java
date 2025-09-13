package com.example.tacocloud.controller;

import com.example.tacocloud.converter.IngredientByIdConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final IngredientByIdConverter ingredientByIdConverter;

    public WebConfig(IngredientByIdConverter ingredientByIdConverter) {
        this.ingredientByIdConverter = ingredientByIdConverter;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("home");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(ingredientByIdConverter);
    }
}
