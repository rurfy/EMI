package com.example.emi.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.emi.view.LayoutUtils;
import com.example.emi.model.APIConnector;
import com.example.emi.R;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class EditTicketController extends AppCompatActivity {

    Button buttonCreate;
    Button buttonCancel;
    EditText inputTitle;
    EditText inputCreator;
    EditText inputProblem;
    Spinner spinnerStatus;
    LinearLayout checkBoxContainer;
    ArrayList<Integer> ticketId = new ArrayList<>();
    ArrayList<CheckBox> checkBoxesCategories = new ArrayList<>();
    ArrayList<HashMap<String, String>> statusList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_ticket);

        Bundle b = getIntent().getExtras();
        if(b != null) {
            ticketId.add(0, b.getInt("key"));
        } else {
            Log.e("FehlerID", "keine ID eingetragen");
        }

        inputTitle = findViewById(R.id.textInputEditTextTitle);
        inputProblem = findViewById(R.id.textInputEditTextProblem);
        //inputCreator = (EditText)findViewById(R.id.textInputEditTextTitle);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        checkBoxContainer = findViewById(R.id.linLayCheckBoxes);


        // Buttons Bezeichnung zuweisen
        buttonCreate = findViewById(R.id.buttonLeft);
        buttonCreate.setText(R.string.ok);
        buttonCancel = findViewById(R.id.buttonRight);
        buttonCancel.setText(R.string.cancel);


        APIConnector.get("Status", null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);

                statusList = JSONUtils.jsonToArrayListHash(response);

                LayoutUtils.setDropDownStatusContent(spinnerStatus, EditTicketController.this, statusList);


                APIConnector.get("Ticket/" + ticketId.get(0), null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                        super.onSuccess(statusCode, headers, response);
                        HashMap<String, String> ticketDataMap = JSONUtils.jsonToArrayListHash(response).get(0);

                        LayoutUtils.setStaticContent(inputTitle, inputProblem, ticketDataMap, true);
                        LayoutUtils.setSelectedStatus(spinnerStatus, ticketDataMap, statusList);

                        APIConnector.get("Ticket_hat_Kategorie/1/TicketID", null, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                                super.onSuccess(statusCode, headers, response);

                                final ArrayList<String> selectedCategories = JSONUtils.jsonToArrayListString(response, "KategorieID");

                                APIConnector.get("Kategorie", null, new JsonHttpResponseHandler() {

                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {


                                        super.onSuccess(statusCode, headers, response);

                                        HashMap<String, String> categoriesHashMap = JSONUtils.jsonArraytoHashMap(response);


                                        checkBoxesCategories = LayoutUtils.setSelectedCategoriesCheckBox(checkBoxContainer, EditTicketController.this, categoriesHashMap, selectedCategories);
                                        int x = checkBoxesCategories.size();
                                        Log.e("Size", Integer.toString(x));

                                    }
                                });
                            }
                        });
                    }
                });
            }
        });


        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (inputTitle.getText().toString().equals("")) {
                    Toast.makeText(EditTicketController.this, "Bitte geben Sie einen Titel für das Ticket ein.",
                            Toast.LENGTH_LONG).show();
                }
                else if (inputProblem.getText().toString().equals("")) {
                    Toast.makeText(EditTicketController.this, "Bitte geben Sie eine Problembeschreibung ein.",
                            Toast.LENGTH_LONG).show();
                }
                else {

                    HashMap<String, String> ticketDataMap = LayoutUtils.getStaticContent(inputTitle, inputProblem, EditTicketController.this);
                    String statID = LayoutUtils.getStatus(spinnerStatus, statusList);
                    ticketDataMap.put("StatusID", statID);

                    ArrayList<String> selectedCategories = LayoutUtils.getSelectedCategories(checkBoxesCategories);


                // TODO Update Ticket in DB

                    Intent toShowTicket = new Intent(EditTicketController.this, ShowTicketController.class);
                    Bundle b = new Bundle();
                    b.putInt("key", ticketId.get(0));
                    toShowTicket.putExtras(b);
                    startActivity(toShowTicket);
                    finish();
                }
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent toShowTicket = new Intent(EditTicketController.this, ShowTicketController.class);
                Bundle b = new Bundle();
                b.putInt("key", ticketId.get(0));
                toShowTicket.putExtras(b);
                startActivity(toShowTicket);
                finish();
            }
        });
    }
}