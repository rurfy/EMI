package com.example.emi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

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

                //Es wird auf die CreateTicket Activity gewechselt
                Intent toAllTicketsPage = new Intent(MenuController.this, CreateTicket.class);
                startActivity(toAllTicketsPage);

            }
        });

        allTickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

    }

}
