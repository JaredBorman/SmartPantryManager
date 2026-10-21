package com.example.smartpantrymanager;

public class Recipe {
    public long id;
    public String name;
    public String ingredients;
    public String steps;

    public Recipe(long id, String name, String ingredients, String steps) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
    }
}
