package com.example.smartpantrymanager;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class IngredientFormActivity extends Activity {
    private DatabaseHelper db;
    private EditText name, quantity, unit, expiry;
    private long ingredientId = -1;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_form);
        db = new DatabaseHelper(this);
        name = findViewById(R.id.nameInput); quantity = findViewById(R.id.quantityInput);
        unit = findViewById(R.id.unitInput); expiry = findViewById(R.id.expiryInput);
        ingredientId = getIntent().getLongExtra("ingredient_id", -1);
        if (ingredientId != -1) {
            setTitle("Edit Ingredient");
            Ingredient item = db.getIngredient(ingredientId);
            if (item != null) {
                name.setText(item.name); quantity.setText(String.valueOf(item.quantity));
                unit.setText(item.unit); expiry.setText(item.expiry);
            }
        }
        findViewById(R.id.saveIngredient).setOnClickListener(v -> save());
        findViewById(R.id.cancelIngredient).setOnClickListener(v -> finish());
    }
    private void save() {
        String n = name.getText().toString().trim();
        String q = quantity.getText().toString().trim();
        String u = unit.getText().toString().trim();
        String e = expiry.getText().toString().trim();
        if (n.isEmpty() || q.isEmpty() || u.isEmpty()) {
            Toast.makeText(this, "Name, quantity and unit are required.", Toast.LENGTH_LONG).show(); return;
        }
        double amount;
        try { amount = Double.parseDouble(q); }
        catch (NumberFormatException ex) { Toast.makeText(this, "Enter a valid quantity.", Toast.LENGTH_LONG).show(); return; }
        if (Double.isNaN(amount) || Double.isInfinite(amount) || amount <= 0) {
            Toast.makeText(
                    this,
                    "Quantity must be a finite number greater than zero.",
                    Toast.LENGTH_LONG
            ).show();
            return;
        }
        if (ingredientId == -1) {
            long result = db.addIngredient(n, amount, u, e);

            if (result == -1) {
                Toast.makeText(this, "Database insert failed.", Toast.LENGTH_LONG).show();
                return;
            }
        } else {
            int result = db.updateIngredient(ingredientId, n, amount, u, e);

            if (result == 0) {
                Toast.makeText(this, "Ingredient update failed.", Toast.LENGTH_LONG).show();
                return;
            }
        }

        Toast.makeText(this, "Ingredient saved.", Toast.LENGTH_SHORT).show();
        finish();
    }
}
