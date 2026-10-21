package com.example.smartpantrymanager;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.Toast;

public class SettingsActivity extends Activity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        DatabaseHelper db = new DatabaseHelper(this);
        Switch alerts = findViewById(R.id.expiryAlertsSwitch);
        alerts.setChecked(db.getExpiryAlerts());
        alerts.setOnCheckedChangeListener((button, checked) -> {
            db.setExpiryAlerts(checked);
            Toast.makeText(this, "Preference saved.", Toast.LENGTH_SHORT).show();
        });
        findViewById(R.id.backFromSettings).setOnClickListener(v -> finish());
    }
}
