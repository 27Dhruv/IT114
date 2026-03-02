package com.example.listprocessor;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

import dagger.hilt.android.AndroidEntryPoint;
import javax.inject.Inject;

@AndroidEntryPoint
public class AddItemActivity extends AppCompatActivity {

    @Inject
    ItemList the_list;   // reference to singleton string list object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_item);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    } // end onCreate

    // add the item to the list

    public void addItem(View view)
    {

        EditText et1;
        EditText et2;
        String new_item;
        int pos;

        // get the new item and its desired position

        // get the new item and its desired position

        et1 = (EditText) findViewById(R.id.edit_item);
        new_item = et1.getText().toString();

        et2 = (EditText) findViewById(R.id.edit_position);
        pos = Integer.parseInt(et2.getText().toString());


        // try to put the new item on the list

        try
        {
            the_list.add(pos, new_item);

        // hide soft keyboard so snackbar is visible

            hideKeyboard();
            Snackbar.make(findViewById(R.id.myCoordinatorLayout),
                    new_item + " added to the list",
                    Snackbar.LENGTH_SHORT).show();
        }
        catch(IndexOutOfBoundsException e)
        {
            hideKeyboard();
            Snackbar.make(findViewById(R.id.myCoordinatorLayout),
                    "Failed to add item to the list",
                    Snackbar.LENGTH_SHORT).show();

        }

    } // end addItem

    private void hideKeyboard()
    {
        // This method dismisses the soft keyboard.
        // Code derived from developer.android.com and
        // StackOverflow

        Context context = getCurrentFocus().getContext();

        InputMethodManager inputMethodManager =
                (InputMethodManager)
                        context.getSystemService(Activity.INPUT_METHOD_SERVICE);

        if (inputMethodManager != null)
            inputMethodManager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);

    } // end hideKeyboard

} // end AddItemActivity
