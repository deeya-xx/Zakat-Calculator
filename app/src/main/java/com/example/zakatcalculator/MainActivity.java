package com.example.zakatcalculator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Set up the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Zakat Calculator");
        }

        // Bind views
        EditText goldValueInput = findViewById(R.id.editTextNumberDecimal);
        EditText goldWeightInput = findViewById(R.id.editTextNumberDecimal2);
        Spinner goldTypeSpinner = findViewById(R.id.spinner);
        EditText zakatValueOutput = findViewById(R.id.editTextNumberDecimal3);
        Button calculateButton = findViewById(R.id.button);
        ImageButton shareButton = findViewById(R.id.shareButton);
        ImageButton aboutButton = findViewById(R.id.aboutButton);
        aboutButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        });

        // Populate Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gold_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        goldTypeSpinner.setAdapter(adapter);

        // Set up calculation button
        calculateButton.setOnClickListener(v -> {
            try {

                // Validate input for gold value
                String goldValueText = goldValueInput.getText().toString();
                String goldWeightText = goldWeightInput.getText().toString();

                if (!isValidNumber(goldValueText) || !isValidNumber(goldWeightText)) {
                    Toast.makeText(this, "Invalid input. Please enter valid numbers.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Get input values
                double goldValue = Double.parseDouble(goldValueInput.getText().toString());
                double goldWeight = Double.parseDouble(goldWeightInput.getText().toString());
                String goldType = goldTypeSpinner.getSelectedItem().toString();

                // Determine uruf based on gold type
                double uruf = goldType.equals("Keep") ? 85 : 200;

                // Calculate zakat
                double zakatWeight = goldWeight - uruf;
                double zakatPayableValue = zakatWeight > 0 ? zakatWeight * goldValue : 0;
                double zakat = 0.025 * zakatPayableValue;

                // Display zakat value
                zakatValueOutput.setText(String.format("RM %.2f", zakat));

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Unexpected error occurred.", Toast.LENGTH_SHORT).show();
            }
        });
        // Set up share button
        shareButton.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String shareMessage = "Check out this Zakat Calculator app: https://example.com/zakat-calculator";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    // Method to validate numeric input
    private boolean isValidNumber(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
