package com.example.lab4redo;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

// Full image URL for grading:
// https://web.njit.edu/~YOURUCID/mypic.jpeg

public class MainActivity extends AppCompatActivity {

    private EditText urlInput;
    private Button displayButton;
    private SimpleDraweeView imageArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Fresco
        Fresco.initialize(this);

        setContentView(R.layout.activity_main);

        urlInput = findViewById(R.id.url_input);
        displayButton = findViewById(R.id.display_button);
        imageArea = findViewById(R.id.image_area);

        displayButton.setOnClickListener(v -> {
            String imageUrl = urlInput.getText().toString().trim();

            if (!imageUrl.isEmpty()) {
                imageArea.setImageURI(Uri.parse(imageUrl));
            }
        });
    }
}