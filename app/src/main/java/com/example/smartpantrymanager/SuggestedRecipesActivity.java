package com.example.smartpantrymanager;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class SuggestedRecipesActivity extends Activity {
    private DatabaseHelper db;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested);
        db = new DatabaseHelper(this);
        findViewById(R.id.backToPantry).setOnClickListener(v -> finish());
    }
    @Override protected void onResume() {
        super.onResume();
        ArrayList<Recipe> recipes = db.getSuggestedRecipes();
        ListView list = findViewById(R.id.suggestedList);
        TextView empty = findViewById(R.id.noSuggestions);
        list.setAdapter(new RecipeAdapter(this, recipes));
        empty.setText(recipes.isEmpty() ? "No recipes match your pantry yet. Add more ingredients and try again." : "");
    }
}
