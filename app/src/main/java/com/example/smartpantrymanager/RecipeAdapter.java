package com.example.smartpantrymanager;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class RecipeAdapter extends ArrayAdapter<Recipe> {
    private final Context context;
    public RecipeAdapter(Context context, ArrayList<Recipe> recipes) {
        super(context, android.R.layout.simple_list_item_2, recipes);
        this.context = context;
    }
    @Override public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView == null ? LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false) : convertView;
        Recipe recipe = getItem(position);
        TextView title = row.findViewById(android.R.id.text1);
        TextView detail = row.findViewById(android.R.id.text2);
        title.setText(recipe.name);
        detail.setText("Ingredients: " + recipe.ingredients.replace(",", ", "));
        row.setOnClickListener(v -> {
            Intent i = new Intent(context, RecipeDetailActivity.class);
            i.putExtra("recipe_id", recipe.id);
            context.startActivity(i);
        });
        return row;
    }
}
