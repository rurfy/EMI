package com.example.emi.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.emi.R;
import com.example.emi.model.OnJSONResponseCallback;
import com.example.emi.model.RestUtils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class AllTicketsController extends AppCompatActivity {

    ArrayList<HashMap<String, String>> list;
    ListView lvAllTickets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_tickets);

        lvAllTickets = (ListView) findViewById(R.id.ticketList);
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

        lvAllTickets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(AllTicketsController.class.getSimpleName(), lvAllTickets.getItemAtPosition(position).toString());

            }
        });


        RestUtils.getAllItems("Ticket", AllTicketsController.this, new OnJSONResponseCallback() {
            @Override
            public void onJSONResponse(JSONArray response) {
                list = JSONUtils.jsonToArrayListHash(response);
                ListAdapter adapter = new SimpleAdapter(AllTicketsController.this, list, R.layout.all_tickets_item,
                        new String[]{"ID", "Titel", "Datum"}, new int[]{R.id.itemID, R.id.itemTitle, R.id.itemDate});
                lvAllTickets.setAdapter(adapter);

            }

            @Override
            public void onJSONResponse(String id) {

            }
        });


    }
}
