package com.example.emi.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emi.view.LayoutUtils;
import com.example.emi.model.APIConnector;
import com.example.emi.model.RestUtils;
import com.example.emi.R;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class CreateTicketController extends AppCompatActivity {

        Button buttonCreate;
        Button buttonCancel;
        EditText inputTitle;
        EditText inputCreator;
        EditText inputProblem;
        TextView textViewStatus;
        Spinner spinnerStatus;
        LinearLayout checkBoxContainer;

        ArrayList<HashMap<String, String>> statusList;
        HashMap<String, String> categoryMap;
        ArrayList<CheckBox> checkers;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.single_ticket);

            buttonCreate = findViewById(R.id.buttonLeft);
            buttonCreate.setText(R.string.ok);
            buttonCancel = findViewById(R.id.buttonRight);
            buttonCancel.setText(R.string.cancel);

            // Input Felder mit der xml-Datei verknüpft
            inputTitle = findViewById(R.id.textInputEditTextTitle);
            inputProblem = findViewById(R.id.textInputEditTextProblem);

            checkBoxContainer = findViewById(R.id.linLayCheckBoxes);
            spinnerStatus = findViewById(R.id.spinnerStatus);

            //Status aus der DB lesen und sowohl in den Spinner, als auch in eine ArrayList schreiben
            APIConnector.get("Status", null, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);

                    statusList = JSONUtils.jsonToArrayListHash(response);
                    LayoutUtils.setDropDownStatusContent(spinnerStatus, CreateTicketController.this, statusList);

                }
            });


            //Kategorien aus der DB lesen und sowohl in die CheckListe, als auch in eine ArrayList schreiben
            APIConnector.get("Kategorie", null, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);

                    categoryMap = JSONUtils.jsonArraytoHashMap(response);
                    checkers = LayoutUtils.setAllCategories(checkBoxContainer, CreateTicketController.this, categoryMap);
                }
            });


            //Wird ausgeführt wenn der Nutzer den OK-Button drückt
            buttonCreate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e(CreateTicketController.this.getLocalClassName(), "Der OK Button wurde benutzt.");

                    if (inputTitle.getText().toString().equals("")) {
                        Toast.makeText(CreateTicketController.this, "Bitte geben Sie einen Titel für das Ticket ein.",
                                Toast.LENGTH_LONG).show();
                    }
                    else if (inputProblem.getText().toString().equals("")) {
                        Toast.makeText(CreateTicketController.this, "Bitte geben Sie eine Problembeschreibung ein.",
                                Toast.LENGTH_LONG).show();
                    }
                    else {
                        //Lokale HashMap zur Speicherung der eingegebenen Daten
                        HashMap <String, String> ticketDataMap = LayoutUtils.getStaticContent(inputTitle, inputProblem, CreateTicketController.this);

                        //ausgewählten Status herausfinden und die korrespondierende StatusID speichern
                        String statID = LayoutUtils.getStatus(spinnerStatus, statusList);

                        //ausgewählte Kategorie herausfinden und die korrespondierende KategorieID speichern
                        ArrayList<String> catID = LayoutUtils.getSelectedCategories(checkers);

                        //Daten in die Hashmap schreiben
                        ticketDataMap.put("StatusID", statID);

                        String cats = "[";
                        for (int i = 0; i < catID.size(); i++) {
                            cats += catID.get(i);
                            if (i != catID.size()-1) {
                                cats += ",";
                            }
                        }
                        cats += "]";
                        ticketDataMap.put("KategorieID", cats);

                        //Das einzufügende JSONObject wird mit den Daten befüllt und in die entsprechende Tabelle geladen
                        JSONObject postObject = JSONUtils.prepareDataForPost(ticketDataMap);
                        RestUtils.postOneItem(postObject,"Ticket", CreateTicketController.this);

                        //Intent backToMenu = new Intent(CreateTicketController.this, MenuController.class);
                        //startActivity(backToMenu);

                    }

                }
            });

            //Wird aufgerufen wenn der Cancel-Button betätigt wird
            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Die Inputfelder werden zurückgesetzt
                    inputProblem.setText("");
                    inputTitle.setText("");

                    //Nutzerausgabe, dass abgebrochen wurde
                    Toast.makeText(CreateTicketController.this, "Die Erstellung des Tickets wurde abgebrochen",
                            Toast.LENGTH_LONG).show();

                }
            });
        }



}


