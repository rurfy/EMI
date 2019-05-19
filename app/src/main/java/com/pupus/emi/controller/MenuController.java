package com.pupus.emi.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pupus.emi.R;

public class MenuController extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedBundle) {
        super.onCreate(savedBundle);
        setContentView(R.layout.main_menu);

        //Zurückpfeil unsichtbar machen, da er in dieser View keinen Sinn macht
        ImageView back_arrow = findViewById(R.id.back_arrow);
        back_arrow.setVisibility(View.INVISIBLE);

        //Text vom Titel anpassen
        TextView title = findViewById(R.id.viewCaption);
        title.setText(R.string.menu);

        ///Haus unsichtbar machen, da er in dieser View keinen Sinn macht
        ImageView house = findViewById(R.id.home);
        house.setVisibility(View.INVISIBLE);


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
