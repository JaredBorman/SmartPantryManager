package com.example.smartpantrymanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class MainActivity extends Activity {
    private DatabaseHelper db;
    private ListView list;
    private TextView empty;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(this);
        list = findViewById(R.id.pantryList); empty = findViewById(R.id.emptyPantry);
        findViewById(R.id.addIngredient).setOnClickListener(v -> startActivity(new Intent(this, IngredientFormActivity.class)));
        findViewById(R.id.suggestedButton).setOnClickListener(v -> startActivity(new Intent(this, SuggestedRecipesActivity.class)));
        findViewById(R.id.settingsButton).setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
    }
    @Override protected void onResume() { super.onResume(); refreshIngredients(); }
    public void refreshIngredients() {
        if (db == null) return;
        ArrayList<Ingredient> items = db.getIngredients();
        list.setAdapter(new IngredientAdapter(this, items, db));
        empty.setText(items.isEmpty() ? "Your pantry is empty. Add an ingredient to get started." : "");
    }
}
