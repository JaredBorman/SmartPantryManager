package com.example.smartpantrymanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;

public class IngredientAdapter extends ArrayAdapter<Ingredient> {
    private final Context context;
    private final ArrayList<Ingredient> items;
    private final DatabaseHelper db;

    public IngredientAdapter(Context context, ArrayList<Ingredient> items, DatabaseHelper db) {
        super(context, R.layout.row_ingredient, items);
        this.context = context; this.items = items; this.db = db;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView == null ? LayoutInflater.from(context).inflate(R.layout.row_ingredient, parent, false) : convertView;
        Ingredient item = items.get(position);
        TextView title = row.findViewById(R.id.ingredientTitle);
        TextView detail = row.findViewById(R.id.ingredientDetail);
        Button edit = row.findViewById(R.id.editIngredient);
        Button delete = row.findViewById(R.id.deleteIngredient);
        title.setText(item.name);
        detail.setText(item.quantity + " " + item.unit + (item.expiry.isEmpty() ? "" : " • Expires: " + item.expiry));
        edit.setOnClickListener(v -> {
            Intent i = new Intent(context, IngredientFormActivity.class);
            i.putExtra("ingredient_id", item.id);
            context.startActivity(i);
        });
        delete.setOnClickListener(v -> new AlertDialog.Builder(context)
            .setTitle("Delete ingredient?")
            .setMessage("Remove " + item.name + " from your pantry?")
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Delete", (d, which) -> {
                db.deleteIngredient(item.id);
                if (context instanceof MainActivity) ((MainActivity) context).refreshIngredients();
            }).show());
        return row;
    }
}
