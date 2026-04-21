package com.example.qaclient;

import android.content.Intent;
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
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CommunicateActivity extends AppCompatActivity {

    private TextView tv;
    private EditText etOperation;
    private EditText etNum1;
    private EditText etNum2;

    private String hostname;
    private int port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_communicate);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        hostname = intent.getStringExtra(MainActivity.HOST_NAME);
        port = intent.getIntExtra(MainActivity.PORT, 6000);

        tv = findViewById(R.id.text_answer);
        etOperation = findViewById(R.id.edit_operation);
        etNum1 = findViewById(R.id.edit_num1);
        etNum2 = findViewById(R.id.edit_num2);

        // Hard-coded default test values
        etOperation.setText("add");
        etNum1.setText("5");
        etNum2.setText("3");

        tv.setText("Ready. Host=" + hostname + " Port=" + port);
    }

    public void sendQuestion(View view) {
        String operation = etOperation.getText().toString().trim();
        String num1 = etNum1.getText().toString().trim();
        String num2 = etNum2.getText().toString().trim();

        if (operation.isEmpty()) {
            etOperation.setError("Enter operation");
            return;
        }

        if (num1.isEmpty()) {
            etNum1.setError("Enter first number");
            return;
        }

        if (num2.isEmpty()) {
            etNum2.setError("Enter second number");
            return;
        }

        tv.setText("Sending request...");

        new Thread(() -> {
            Socket socket = null;
            PrintWriter out = null;
            BufferedReader in = null;

            try {
                socket = new Socket(hostname, port);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                out.println(operation);
                out.println(num1);
                out.println(num2);

                String answer = in.readLine();
                String resultText = (answer == null) ? "No response from server." : "Result: " + answer;

                runOnUiThread(() -> tv.setText(resultText));

            } catch (IOException e) {
                runOnUiThread(() -> tv.setText("Problem: " + e.toString()));
            } finally {
                try {
                    if (out != null) out.close();
                    if (in != null) in.close();
                    if (socket != null) socket.close();
                } catch (IOException ignored) {
                }
            }
        }).start();
    }
}