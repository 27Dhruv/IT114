package com.example.labassignment2;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText editFile;
    private TextView textMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Allow network requests on the main thread (for this exercise)
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        editFile = findViewById(R.id.edit_file);
        textMain = findViewById(R.id.text_main);
        Button buttonDisplay = findViewById(R.id.button_display);

        buttonDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayBoats();
            }
        });
    }

    public void displayBoats() {
        String fileUrl = editFile.getText().toString().trim();
        if (fileUrl.isEmpty()) {
            textMain.setText("Please enter a valid URL.");
            return;
        }

        try {
            // Fetch data from the provided URL
            URL url = new URL(fileUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String[] boatData = new String[18];
            String line;
            int index = 0;

            while ((line = reader.readLine()) != null && index < 18) {
                boatData[index++] = line;
            }
            reader.close();

            // Creating Boat objects
            Boat[] boats = new Boat[3];
            for (int i = 0; i < 3; i++) {
                boats[i] = new Boat(
                        boatData[i * 6],                     // Name
                        boatData[i * 6 + 1],                 // Model
                        Integer.parseInt(boatData[i * 6 + 2]), // Year
                        Double.parseDouble(boatData[i * 6 + 3]), // Length
                        boatData[i * 6 + 4],                 // Fuel Type
                        Double.parseDouble(boatData[i * 6 + 5])  // Price
                );
            }

            // Calculate averages
            double avgLength = (boats[0].getLength() + boats[1].getLength() + boats[2].getLength()) / 3.0;
            double avgPrice = (boats[0].getPrice() + boats[1].getPrice() + boats[2].getPrice()) / 3.0;

            // Display results
            StringBuilder result = new StringBuilder();
            for (Boat boat : boats) {
                result.append(boat.toString()).append("\n");
            }
            result.append(String.format("Average Length: %.1f feet\n", avgLength));
            result.append(String.format("Average Price: $%.2f", avgPrice));

            textMain.setText(result.toString());

        } catch (Exception e) {
            textMain.setText("Error fetching or processing data: " + e.getMessage());
        }
    }
}
