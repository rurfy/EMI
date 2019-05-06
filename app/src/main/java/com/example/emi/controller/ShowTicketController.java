package com.example.emi.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.emi.model.OnJSONResponseCallback;
import com.example.emi.model.RestUtils;
import com.example.emi.view.LayoutUtils;
import com.example.emi.R;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;


public class ShowTicketController extends AppCompatActivity {

    Button buttonEdit;
    Button buttonBack;
    EditText inputTitle;
    EditText inputCreator;
    EditText inputProblem;
    TextView textViewStatus;
    LinearLayout categorieLayout;
    ArrayList<Integer> ticketId = new ArrayList<>();
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_ticket);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            ticketId.add(0, b.getInt("key"));
        } else {
            ticketId.add(0, 113);
        }

        context = ShowTicketController.this;

        inputProblem = findViewById(R.id.textInputEditTextProblem);
        inputTitle = findViewById(R.id.textInputEditTextTitle);
        textViewStatus = findViewById(R.id.textViewStatus);
        categorieLayout = findViewById(R.id.linLayCheckBoxes);


        // Buttons Bezeichnung zuweisen
        buttonEdit = findViewById(R.id.buttonLeft);
        buttonEdit.setText(R.string.edit);
        buttonBack = findViewById(R.id.buttonRight);
        buttonBack.setText(R.string.back);





        RestUtils.getAllItems("Ticket/" + ticketId.get(0), context, new OnJSONResponseCallback() {
            @Override
            public void onJSONResponse(JSONArray response) {

                HashMap<String, String> ticketDataMap = JSONUtils.jsonToArrayListHash(response).get(0);

                LayoutUtils.setStaticContent(inputTitle, inputProblem, ticketDataMap, false);
                LayoutUtils.setStatus(textViewStatus, ticketDataMap);

                RestUtils.getAllItems("Ticket_hat_Kategorie/" + ticketId.get(0) + "/TicketID", context, new OnJSONResponseCallback() {
                    @Override
                    public void onJSONResponse(JSONArray response) {

                        final ArrayList <String> selectedCategories = JSONUtils.jsonToArrayListString(response, "KategorieID");

                        RestUtils.getAllItems("Kategorie", ShowTicketController.this, new OnJSONResponseCallback() {
                            @Override
                            public void onJSONResponse(JSONArray response) {

                                HashMap<String, String> categoriesHashMap = JSONUtils.jsonArraytoHashMap(response);

                                LayoutUtils.setSelectedCategoriesTextView(categorieLayout, context, categoriesHashMap, selectedCategories);

                            }

                            //Unwichtig, da man immer ein Array bekommt
                            //Der Fall, dass sich das ändern könnte, sollte jedoch behandelt werden
                            @Override
                            public void onJSONResponse(String id) {

                            }
                        });

                    }

                    //Unwichtig, da man immer ein Array bekommt
                    //Der Fall, dass sich das ändern könnte, sollte jedoch behandelt werden
                    @Override
                    public void onJSONResponse(String id) {

                    }
                });

            }

            //Unwichtig, da man immer ein Array bekommt
            //Der Fall, dass sich das ändern könnte, sollte jedoch behandelt werden
            @Override
            public void onJSONResponse(String id) {

            }
        });

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent toEditPage = new Intent(context, EditTicketController.class);
                Bundle b = new Bundle();
                b.putInt("key", ticketId.get(0));
                toEditPage.putExtras(b);
                startActivity(toEditPage);
                finish();
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO Ziel anpassen
                Intent toAllTicketsPage = new Intent(context, MenuController.class);
                startActivity(toAllTicketsPage);
            }
        });
    }
}