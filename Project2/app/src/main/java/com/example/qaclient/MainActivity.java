package com.example.qaclient;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static final String HOST_NAME = "com.example.qaclient.HOSTNAME";
    public static final String PORT = "com.example.qaclient.PORT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Skip the first screen completely
        Intent intent = new Intent(this, CommunicateActivity.class);
        intent.putExtra(HOST_NAME, "10.0.2.2");
        intent.putExtra(PORT, 6000);
        startActivity(intent);
        finish();
    }
}