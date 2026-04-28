package com.example.lab6populationgrowth;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText startInput, daysInput, rateInput;
    Button calculateButton;
    TextView resultsText;

    public double population(int d, int p_start, double growth_rate) {
        if (d == 0) {
            return p_start;
        } else {
            return population(d - 1, p_start, growth_rate) * (1.0 + growth_rate);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startInput = findViewById(R.id.startInput);
        daysInput = findViewById(R.id.daysInput);
        rateInput = findViewById(R.id.rateInput);
        calculateButton = findViewById(R.id.calculateButton);
        resultsText = findViewById(R.id.resultsText);

        calculateButton.setOnClickListener(v -> {
            int startPop = Integer.parseInt(startInput.getText().toString());
            int days = Integer.parseInt(daysInput.getText().toString());
            double ratePercent = Double.parseDouble(rateInput.getText().toString());
            double rateDecimal = ratePercent / 100.0;

            int finalPop = (int) population(days, startPop, rateDecimal);
            int growth = finalPop - startPop;

            resultsText.setText("Population after " + days + " days is " + finalPop +
                    "\nTotal population growth: " + growth);
        });
    }
}