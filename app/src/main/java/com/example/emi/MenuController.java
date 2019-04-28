package com.example.emi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;

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

                Intent toCreatePage = new Intent(MenuController.this, CreateTicket.class);
                startActivity(toCreatePage);

            }
        });

        allTickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent toAllTicketsPage = new Intent(MenuController.this, All_tickets.class);
                startActivity(toAllTicketsPage);

            }
        });

    }
}
