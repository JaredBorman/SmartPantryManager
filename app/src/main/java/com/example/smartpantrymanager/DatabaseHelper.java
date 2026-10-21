package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "smart_pantry.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) { super(context, DB_NAME, null, DB_VERSION); }

    @Override public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE pantry (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, quantity REAL NOT NULL, unit TEXT NOT NULL, expiry TEXT)");
        db.execSQL("CREATE TABLE recipes (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL UNIQUE, ingredients TEXT NOT NULL, steps TEXT NOT NULL)");
        db.execSQL("CREATE TABLE preferences (key TEXT PRIMARY KEY, value TEXT NOT NULL)");
        seedRecipes(db);
        ContentValues pref = new ContentValues();
        pref.put("key", "expiry_alerts"); pref.put("value", "true");
        db.insert("preferences", null, pref);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Database version 1 has no upgrade migrations yet.
        // When the schema changes, add migration logic here
        // to preserve existing user data.
    }

    private void seedRecipes(SQLiteDatabase db) {
        String[][] data = {
            {"Tomato Egg Scramble","egg,tomato,salt,oil","1. Chop tomato. Heat oil, cook tomato, add beaten egg and salt, then stir until set."},
            {"Garlic Pasta","pasta,garlic,oil,salt","1. Boil pasta in salted water. Gently cook chopped garlic in oil and toss with drained pasta."},
            {"Cheese Toast","bread,cheese","1. Place cheese on bread and toast in a pan or toaster oven until melted."},
            {"Banana Oat Bowl","banana,oats,milk","1. Warm oats with milk. Slice banana over the top and serve."},
            {"Potato Hash","potato,onion,oil,salt","1. Dice potato and onion. Fry in oil until browned and cooked through; season with salt."},
            {"Simple Fried Rice","rice,egg,oil,soy sauce","1. Scramble egg in a hot pan. Add cooked rice and soy sauce, then stir-fry."},
            {"Peanut Butter Toast","bread,peanut butter","1. Toast bread and spread peanut butter evenly."},
            {"Apple Oat Porridge","apple,oats,milk","1. Cook oats in milk. Dice apple and stir it through before serving."},
            {"Cheesy Potato","potato,cheese,salt","1. Boil or microwave potato until tender. Split it open, add cheese and salt."},
            {"Tomato Pasta","pasta,tomato,garlic,oil,salt","1. Cook pasta. Cook chopped tomato and garlic in oil until saucy; season and mix."},
            {"Onion Omelette","egg,onion,salt,oil","1. Beat eggs with chopped onion and salt. Cook in an oiled pan until set."},
            {"Milk Banana Smoothie","banana,milk","1. Blend banana and milk until smooth. Serve immediately."},
            {"Garlic Egg Rice","rice,egg,garlic,oil,salt","1. Cook garlic in oil, add cooked rice, then stir in egg and cook through."},
            {"Cheese Pasta","pasta,cheese,milk,salt","1. Boil pasta. Warm milk and cheese together, stir until smooth, then combine."},
            {"Apple Peanut Toast","bread,apple,peanut butter","1. Spread peanut butter on bread and add thin apple slices."},
            {"Tomato Cheese Toast","bread,tomato,cheese","1. Add sliced tomato and cheese to bread. Toast until the cheese melts."},
            {"Potato Egg Cakes","potato,egg,salt,oil","1. Mash cooked potato, mix with beaten egg and salt, then pan-fry small cakes."},
            {"Oat Banana Pancakes","banana,oats,egg","1. Mash banana, mix with oats and egg, then cook small pancakes in a lightly oiled pan."},
            {"Rice and Beans Bowl","rice,beans,onion,salt","1. Warm cooked rice and beans. Add chopped cooked onion and season."},
            {"Garlic Cheese Toast","bread,garlic,cheese,oil","1. Mix minced garlic with a little oil, spread on bread, top with cheese and toast."}
        };
        for (String[] row : data) {
            ContentValues cv = new ContentValues();
            cv.put("name", row[0]); cv.put("ingredients", row[1]); cv.put("steps", row[2]);
            db.insertWithOnConflict("recipes", null, cv, SQLiteDatabase.CONFLICT_IGNORE);
        }
    }

    public long addIngredient(String name, double quantity, String unit, String expiry) {
        ContentValues cv = new ContentValues();
        cv.put("name", normalize(name)); cv.put("quantity", quantity); cv.put("unit", unit.trim());
        cv.put("expiry", expiry == null ? "" : expiry.trim());
        return getWritableDatabase().insert("pantry", null, cv);
    }

    public int updateIngredient(long id, String name, double quantity, String unit, String expiry) {
        ContentValues cv = new ContentValues();
        cv.put("name", normalize(name)); cv.put("quantity", quantity); cv.put("unit", unit.trim());
        cv.put("expiry", expiry == null ? "" : expiry.trim());
        return getWritableDatabase().update("pantry", cv, "id=?", new String[]{String.valueOf(id)});
    }

    public int deleteIngredient(long id) {
        return getWritableDatabase().delete("pantry", "id=?", new String[]{String.valueOf(id)});
    }

    public ArrayList<Ingredient> getIngredients() {
        ArrayList<Ingredient> list = new ArrayList<>();
        try (Cursor c = getReadableDatabase().rawQuery("SELECT id,name,quantity,unit,expiry FROM pantry ORDER BY name COLLATE NOCASE", null)) {
            while (c.moveToNext()) list.add(new Ingredient(c.getLong(0), c.getString(1), c.getDouble(2), c.getString(3), c.getString(4)));
        }
        return list;
    }

    public Ingredient getIngredient(long id) {
        try (Cursor c = getReadableDatabase().rawQuery("SELECT id,name,quantity,unit,expiry FROM pantry WHERE id=?", new String[]{String.valueOf(id)})) {
            if (c.moveToFirst()) return new Ingredient(c.getLong(0), c.getString(1), c.getDouble(2), c.getString(3), c.getString(4));
        }
        return null;
    }

    public ArrayList<Recipe> getSuggestedRecipes() {
        ArrayList<Recipe> suggestions = new ArrayList<>();
        Map<String, Double> pantry = new HashMap<>();
        for (Ingredient item : getIngredients()) pantry.put(normalize(item.name), item.quantity);
        try (Cursor c = getReadableDatabase().rawQuery("SELECT id,name,ingredients,steps FROM recipes ORDER BY name COLLATE NOCASE", null)) {
            while (c.moveToNext()) {
                String[] required = c.getString(2).split(",");
                boolean matches = true;
                for (String raw : required) {
                    String key = normalize(raw);
                    if (!pantry.containsKey(key) || pantry.get(key) < 1.0) { matches = false; break; }
                }
                if (matches) suggestions.add(new Recipe(c.getLong(0), c.getString(1), c.getString(2), c.getString(3)));
            }
        }
        return suggestions;
    }

    public Recipe getRecipe(long id) {
        try (Cursor c = getReadableDatabase().rawQuery("SELECT id,name,ingredients,steps FROM recipes WHERE id=?", new String[]{String.valueOf(id)})) {
            if (c.moveToFirst()) return new Recipe(c.getLong(0), c.getString(1), c.getString(2), c.getString(3));
        }
        return null;
    }

    public boolean getExpiryAlerts() {
        try (Cursor c = getReadableDatabase().rawQuery("SELECT value FROM preferences WHERE key='expiry_alerts'", null)) {
            return c.moveToFirst() && Boolean.parseBoolean(c.getString(0));
        }
    }

    public void setExpiryAlerts(boolean enabled) {
        ContentValues cv = new ContentValues(); cv.put("key", "expiry_alerts"); cv.put("value", String.valueOf(enabled));
        getWritableDatabase().insertWithOnConflict("preferences", null, cv, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public static String normalize(String value) {
        String s = value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
        if (s.endsWith("ies") && s.length() > 3) s = s.substring(0, s.length()-3) + "y";
        else if (s.endsWith("oes") && s.length() > 3) s = s.substring(0, s.length()-2);
        else if (s.endsWith("s") && !s.endsWith("ss") && s.length() > 2) s = s.substring(0, s.length()-1);
        return s;
    }
}
