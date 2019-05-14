package com.example.emi.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.emi.R;

public class MenuController extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedBundle) {
        super.onCreate(savedBundle);
        setContentView(R.layout.main_menu);

        Button createTicket = findViewById(R.id.createTicketButton);
        Button allTickets = findViewById(R.id.viewAllTicketsButton);

        createTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Es wird auf die CreateTicketController Activity gewechselt
                Intent toAllTicketsPage = new Intent(MenuController.this, CreateTicketController.class);
                startActivity(toAllTicketsPage);

            }
        });

        allTickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent toAllTicketsPage = new Intent(MenuController.this, AllTicketsController.class);
                startActivity(toAllTicketsPage);


            }
        });

    }

}
