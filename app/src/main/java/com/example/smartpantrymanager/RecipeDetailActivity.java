package com.example.smartpantrymanager;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class RecipeDetailActivity extends Activity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        DatabaseHelper db = new DatabaseHelper(this);
        Recipe recipe = db.getRecipe(getIntent().getLongExtra("recipe_id", -1));
        TextView title = findViewById(R.id.recipeTitle), ingredients = findViewById(R.id.recipeIngredients), steps = findViewById(R.id.recipeSteps);
        if (recipe == null) { title.setText("Recipe not found"); }
        else {
            title.setText(recipe.name);
            ingredients.setText("Required ingredients\n" + recipe.ingredients.replace(",", "\n• "));
            steps.setText("Preparation\n" + recipe.steps);
        }
        findViewById(R.id.backFromRecipe).setOnClickListener(v -> finish());
    }
}
