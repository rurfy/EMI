package com.example.emi.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.emi.view.LayoutUtils;
import com.example.emi.model.APIConnector;
import com.example.emi.R;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class ShowTicketController extends AppCompatActivity {

    Button buttonEdit;
    Button buttonBack;
    EditText inputTitle;
    EditText inputCreator;
    EditText inputProblem;
    TextView textViewStatus;
    LinearLayout categorieLayout;
    ArrayList<Integer> ticketId = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_ticket);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            ticketId.add(0, b.getInt("key"));
        } else {
            ticketId.add(0, 1);
        }

        inputProblem = findViewById(R.id.textInputEditTextProblem);
        inputTitle = findViewById(R.id.textInputEditTextTitle);
        textViewStatus = findViewById(R.id.textViewStatus);
        categorieLayout = findViewById(R.id.linLayCheckBoxes);


        // Buttons Bezeichnung zuweisen
        buttonEdit = findViewById(R.id.buttonLeft);
        buttonEdit.setText(R.string.edit);
        buttonBack = findViewById(R.id.buttonRight);
        buttonBack.setText(R.string.back);


        APIConnector.get("Ticket/" + ticketId.get(0), null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                super.onSuccess(statusCode, headers, response);
                HashMap<String, String> ticketDataMap = JSONUtils.jsonToArrayListHash(response).get(0);

                LayoutUtils.setStaticContent(inputTitle, inputProblem, ticketDataMap, false);
                LayoutUtils.setStatus(textViewStatus, ticketDataMap);

                APIConnector.get("Ticket_hat_Kategorie/" + ticketId.get(0) + "/TicketID", null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                        super.onSuccess(statusCode, headers, response);

                        final ArrayList<String> selectedCategories = JSONUtils.jsonToArrayListString(response, "KategorieID");


                        APIConnector.get("Kategorie", null, new JsonHttpResponseHandler() {

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {


                                super.onSuccess(statusCode, headers, response);

                                HashMap<String, String> categoriesHashMap = JSONUtils.jsonArraytoHashMap(response);

                                LayoutUtils.setSelectedCategoriesTextView(categorieLayout, ShowTicketController.this, categoriesHashMap, selectedCategories);
                            }
                        });
                    }
                });
            }
        });

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent toEditPage = new Intent(ShowTicketController.this, EditTicketController.class);
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
                Intent toAllTicketsPage = new Intent(ShowTicketController.this, MenuController.class);
                startActivity(toAllTicketsPage);
            }
        });
    }
}