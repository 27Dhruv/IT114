package com.example.listprocessor;

import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;

/////////////////////////////////////////////////////
//
// This app manipulates a list of strings.  Options
// are chosen from the main app bar.
//
// Some options just display results in the main
// TextView.  Others invoke new activities to carry
// out their tasks.
//
// Author: M. Halper
//
/////////////////////////////////////////////////////

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    @Inject
    ItemList the_list;   // reference to singleton string list object

    private TextView tv;

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

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // set the reference to the "main" TextView object so
        // we do not have to retrieve it in every method below

           tv = (TextView) findViewById(R.id.text_main);

        // put some strings on the list (if the list is empty).  Note that the
        // "new" list might not be empty due to a restart of the app

        if(the_list.isEmpty())
        {

            the_list.add(the_list.size(), "pizza");
            the_list.add(the_list.size(), "crackers");
            the_list.add(the_list.size(), "peanut butter");
            the_list.add(the_list.size(), "jelly");
            the_list.add(the_list.size(), "bread");
            the_list.add(the_list.size(), "spaghetti");

        }

    } // end onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onOption1(MenuItem i)
    {
        // show the list in order

        int j;

        tv.setText("");

        // display the list items

        for(j = 0; j < the_list.size(); j++)

            tv.append(the_list.get(j) + '\n');

    } // end onOption1


    public void onOption2(MenuItem i)
    {
        // show the list in reverse order

        int j;

        tv.setText("");

        for (j = the_list.size() - 1; j >= 0; j--)
            tv.append(the_list.get(j) + '\n');

    } // end onOption2

    public void onOption3(MenuItem i)
    {
        // show the list size

        tv.setText("The list currently has " + the_list.size() + " items.");

    } // end onOption3


    public void onOption4(MenuItem i)
    {

        // Start the activity to add a new item to the list

        startActivity(new Intent(this, AddItemActivity.class));

    } // end onOption4

    public void onOption5(MenuItem i)
    {
        // Start the activity to remove an item from the list
        startActivity(new Intent(this, RemoveItemActivity.class));

    } // end onOption5

    public void onOption6(MenuItem i)
    {
        // Remove all items from the list

        the_list.clear();
        tv.setText("All items removed. The list is now empty.");

    } // end onOption6

} // end MainActivity
