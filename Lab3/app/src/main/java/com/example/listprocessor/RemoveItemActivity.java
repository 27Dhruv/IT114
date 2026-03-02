package com.example.listprocessor;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RemoveItemActivity extends AppCompatActivity {

    @Inject
    ItemList the_list;

    private EditText etItem;
    private TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        etItem = (EditText) findViewById(R.id.edit_item);
        tvStatus = (TextView) findViewById(R.id.text_status);

        // Change heading so the user knows this is REMOVE mode
        TextView heading = (TextView) findViewById(R.id.text_add_heading);
        heading.setText("Remove a String from the List");
    }

    // This matches android:onClick="addItem" in the XML
    public void addItem(View view) {
        String item = etItem.getText().toString();

        try {
            int index = the_list.indexOf(item);

            if (index >= 0) {
                the_list.remove(index);
                tvStatus.setText("Item removed.");
            } else {
                tvStatus.setText("Item not found.");
            }
        } catch (Exception e) {
            tvStatus.setText("Error removing item.");
        }
    }
}