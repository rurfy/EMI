package com.example.emi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class All_tickets extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_tickets);

        ImageView back_arrow = (ImageView) findViewById(R.id.back_arrow);
        back_arrow.setVisibility(View.INVISIBLE);

        ImageView house = (ImageView) findViewById(R.id.home);
        house.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToHome = new Intent(All_tickets.this, MainActivity.class);
                startActivity(backToHome);
            }
        });

    }
}
