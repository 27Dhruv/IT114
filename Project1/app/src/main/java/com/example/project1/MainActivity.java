package com.example.project1;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private ItemList itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Central Avenue Realty");
        }

        textView = findViewById(R.id.textView);
        itemList = ItemList.getInstance();

        textView.setText(getString(R.string.welcome_message));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_display) {
            displayList();
            return true;
        } else if (id == R.id.menu_load_web) {
            promptForWebFile();
            return true;
        } else if (id == R.id.menu_add_asset) {
            promptForAssetFile();
            return true;
        } else if (id == R.id.menu_details) {
            promptForLotDetails();
            return true;
        } else if (id == R.id.menu_remove) {
            promptForLotRemove();
            return true;
        } else if (id == R.id.menu_avg_tax) {
            showWeightedAvgTax();
            return true;
        } else if (id == R.id.menu_mansions) {
            showMansionCount();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displayList() {
        ArrayList<House> houses = itemList.getList();

        if (houses.isEmpty()) {
            textView.setText("The list is empty.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (House h : houses) {
            sb.append(h.getStreetAddress())
                    .append(", ")
                    .append(h.getCity())
                    .append(", ")
                    .append(h.getLotNumber())
                    .append(", ")
                    .append(h.getSquareFootage())
                    .append(" sqft, ")
                    .append(h.getYearBuilt())
                    .append("\n");
        }

        textView.setText(sb.toString());
    }

    private void promptForWebFile() {
        EditText input = new EditText(this);

        new AlertDialog.Builder(this)
                .setTitle("Enter web file URL")
                .setView(input)
                .setPositiveButton("OK", (dialog, which) -> {
                    String url = input.getText().toString().trim();
                    loadFromWebFile(url);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void loadFromWebFile(String fileUrl) {
        new Thread(() -> {
            try {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(new URL(fileUrl).openStream())
                );

                itemList.clear();

                while (true) {
                    String street = br.readLine();
                    if (street == null) break;

                    String city = br.readLine();
                    String lot = br.readLine();
                    double price = Double.parseDouble(br.readLine());
                    double taxes = Double.parseDouble(br.readLine());
                    int sqft = Integer.parseInt(br.readLine());
                    int yearBuilt = Integer.parseInt(br.readLine());

                    House house = new House(street, city, lot, price, taxes, sqft, yearBuilt);
                    itemList.addHouse(house);
                }

                br.close();

                runOnUiThread(() -> textView.setText("Web file loaded successfully."));
            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(this, "Web file not found or invalid.", Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }

    private void promptForAssetFile() {
        EditText input = new EditText(this);

        new AlertDialog.Builder(this)
                .setTitle("Enter asset file name")
                .setView(input)
                .setPositiveButton("OK", (dialog, which) -> {
                    String fileName = input.getText().toString().trim();
                    addHouseFromAsset(fileName);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void addHouseFromAsset(String fileName) {
        try {
            InputStream is = getAssets().open(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String street = br.readLine();
            String city = br.readLine();
            String lot = br.readLine();
            double price = Double.parseDouble(br.readLine());
            double taxes = Double.parseDouble(br.readLine());
            int sqft = Integer.parseInt(br.readLine());
            int yearBuilt = Integer.parseInt(br.readLine());

            House house = new House(street, city, lot, price, taxes, sqft, yearBuilt);
            itemList.addHouse(house);

            br.close();
            textView.setText("House added from asset file.");
        } catch (Exception e) {
            Toast.makeText(this, "Asset file not found or invalid.", Toast.LENGTH_SHORT).show();
        }
    }

    private void promptForLotDetails() {
        EditText input = new EditText(this);

        new AlertDialog.Builder(this)
                .setTitle("Enter lot number")
                .setView(input)
                .setPositiveButton("OK", (dialog, which) -> {
                    String lot = input.getText().toString().trim();
                    House h = itemList.findByLotNumber(lot);

                    if (h == null) {
                        Toast.makeText(this, "Lot number not found.", Toast.LENGTH_SHORT).show();
                    } else {
                        textView.setText(h.toDetailsString());
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void promptForLotRemove() {
        EditText input = new EditText(this);

        new AlertDialog.Builder(this)
                .setTitle("Enter lot number to remove")
                .setView(input)
                .setPositiveButton("OK", (dialog, which) -> {
                    String lot = input.getText().toString().trim();
                    boolean removed = itemList.removeByLotNumber(lot);

                    if (removed) {
                        textView.setText("House removed.");
                    } else {
                        Toast.makeText(this, "Lot number not found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showWeightedAvgTax() {
        ArrayList<House> houses = itemList.getList();

        if (houses.isEmpty()) {
            textView.setText("The list is empty.");
            return;
        }

        double numerator = 0;
        double denominator = 0;

        for (House h : houses) {
            numerator += h.getSquareFootage() * h.getPropertyTaxes();
            denominator += h.getSquareFootage();
        }

        double avg = numerator / denominator;
        textView.setText("Weighted Avg Tax: $" + String.format("%.2f", avg));
    }

    private void showMansionCount() {
        int count = 0;

        for (House h : itemList.getList()) {
            if (h.getSquareFootage() >= House.MANSION_THRESHOLD) {
                count++;
            }
        }

        textView.setText("Number of mansions: " + count);
    }

}