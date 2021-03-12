package com.andflow.cekongkir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class BiayaActivity extends AppCompatActivity {

    TextView tvTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biaya);

        Intent intent = getIntent();
        String origin =intent.getStringExtra("origin");
        String destination =intent.getStringExtra("destination");
        int weight = intent.getIntExtra("weight", 0);
        tvTest = findViewById(R.id.test);
        tvTest.setText(origin);
    }
}