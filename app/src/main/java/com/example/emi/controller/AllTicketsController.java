package com.example.emi.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.emi.R;

public class AllTicketsController extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_tickets);

        ImageView back_arrow = findViewById(R.id.back_arrow);
        back_arrow.setVisibility(View.INVISIBLE);

        ImageView house = findViewById(R.id.home);
        house.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToHome = new Intent(AllTicketsController.this, MenuController.class);
                startActivity(backToHome);
            }
        });

    }
}
