package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;
import java.io.File;


public class MainActivity extends AppCompatActivity {

    private EditText editInFile;
    private EditText editOutFile;
    private TextView textMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Hook up GUI widgets
        editInFile = findViewById(R.id.edit_infile);
        editOutFile = findViewById(R.id.edit_outfile);
        textMain = findViewById(R.id.text_main);
    }

    public void processPress(View view) throws java.io.IOException {
        try {
            String inName = editInFile.getText().toString().trim();
            String outName = editOutFile.getText().toString().trim();

            if (inName.isEmpty()) {
                textMain.setText("Please enter the input file name.");
                return;
            }
            if (outName.isEmpty()) {
                textMain.setText("Please enter the output file name.");
                return;
            }

            // Read doubles from assets/<inName>
            ArrayList<Double> vals = new ArrayList<>();
            InputStream is = getAssets().open(inName);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                vals.add(Double.parseDouble(line));
            }
            br.close();

            if (vals.isEmpty()) {
                textMain.setText("Input file had no numbers.");
                return;
            }

            // Copy to array
            int num_vals = vals.size();
            double[] a = new double[num_vals];
            for (int i = 0; i < num_vals; i++) {
                a[i] = vals.get(i);
            }

            // Convert to 4th roots
            fourth_root_it(a, num_vals);

            // Build output (5 decimals, one per line)
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < num_vals; i++) {
                sb.append(String.format(Locale.US, "%.5f", a[i])).append("\n");
            }

            // Show in TextView
            textMain.setText(sb.toString());

            FileOutputStream fos = openFileOutput(outName, MODE_PRIVATE);
            fos.write(sb.toString().getBytes());
            fos.close();


        } catch (Exception e) {
            textMain.setText("Error: " + e.getMessage());
        }
    } // end processPress

    public void fourth_root_it(double[] a, int num_vals) {
        for (int i = 0; i < num_vals; i++) {
            // 4th root = x^0.25
            a[i] = Math.pow(a[i], 0.25);
        }
    } // end fourth_root_it
}
