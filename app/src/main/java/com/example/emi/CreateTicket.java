package com.example.emi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class CreateTicket extends AppCompatActivity {

        Button buttonCreate;
        Button buttonCancel;
        EditText inputTitle;
        EditText inputCreator;
        EditText inputProblem;
        Spinner spinnerCategory;
        TextView textViewStatus;
        Spinner spinnerStatus;

        ArrayList<HashMap<String, String>> statusList;
    ArrayList<HashMap<String, String>> categoryList;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.show_ticket);

            buttonCreate = (Button)findViewById(R.id.buttonLeft);
            buttonCreate.setText(R.string.ok);
            buttonCancel = (Button) findViewById(R.id.buttonRight);
            buttonCancel.setText(R.string.cancel);


            //Status aus der DB lesen und sowohl in den Spinner, als auch in eine ArrayList schreiben
            APIConnector.get("Status", null, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);

                    statusList = Utils.jsonToArrayList(response);
                    List<String> spinnerArray =  new ArrayList<>();

                    spinnerArray.add(statusList.get(0).get("Bezeichnung"));
                    spinnerArray.add(statusList.get(1).get("Bezeichnung"));
                    spinnerArray.add(statusList.get(2).get("Bezeichnung"));

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            CreateTicket.this, android.R.layout.simple_spinner_item, spinnerArray);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerStatus = (Spinner) findViewById(R.id.spinnerStatus);
                    spinnerStatus.setAdapter(adapter);

                }
            });


            //Kategorien aus der DB lesen und sowohl in den Spinner, als auch in eine ArrayList schreiben
            APIConnector.get("Kategorie", null, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);

                    categoryList = Utils.jsonToArrayList(response);
                    List<String> spinnerArray =  new ArrayList<>();

                    spinnerArray.add(categoryList.get(0).get("Bezeichnung"));
                    spinnerArray.add(categoryList.get(1).get("Bezeichnung"));
                    spinnerArray.add(categoryList.get(2).get("Bezeichnung"));

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            CreateTicket.this, android.R.layout.simple_spinner_item, spinnerArray);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);
                    spinnerCategory.setAdapter(adapter);

                }
            });

            // Input Felder mit der xml-Datei verknüpft
            inputTitle = (EditText)findViewById(R.id.textInputEditTextTitle);
            inputProblem = (EditText)findViewById(R.id.textInputEditTextProblem);


            //Wird ausgeführt wenn der Nutzer den OK-Button drückt
            buttonCreate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e(CreateTicket.this.getLocalClassName(), "Der OK Button wurde benutzt.");

                    //Lokale HashMap zur Speicherung der eingegebenen Daten
                    HashMap <String, String> ticketDataMap = new HashMap<>();

                    //Eingaben auslesen und in String speichern
                    String title = inputTitle.getText().toString();
                    String problem = inputProblem.getText().toString();

                    //aktuelles Datum einlesen und in gewünschtem Format abspeichern
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = new Date();
                    String datum = format.format(date);

                    //ausgewählten Status herausfinden und die korrespondierende StatusID speichern
                    String status = spinnerStatus.getSelectedItem().toString();
                    String statID = "";
                    for (int i = 0; i < statusList.size(); i++) {
                        if (status.equals(statusList.get(i).get("Bezeichnung"))) {
                            statID = statusList.get(i).get("ID");
                        }
                    }

                    //ausgewählte Kategorie herausfinden und die korrespondierende KategorieID speichern
                    String category = spinnerCategory.getSelectedItem().toString();
                    String catID = "";
                    for (int i = 0; i < categoryList.size(); i++) {
                        if (category.equals(categoryList.get(i).get("Bezeichnung"))) {
                            catID = categoryList.get(i).get("ID");
                        }
                    }

                    if (title.equals("")) {
                        Toast.makeText(CreateTicket.this, "Bitte geben Sie einen Titel für das Ticket ein.",
                                Toast.LENGTH_LONG).show();
                    }
                    else if (problem.equals("")) {
                        Toast.makeText(CreateTicket.this, "Bitte geben Sie eine Problembeschreibung ein.",
                                Toast.LENGTH_LONG).show();
                    }
                    else {

                        //Daten in die Hashmap schreiben
                        ticketDataMap.put("Titel", title);
                        ticketDataMap.put("Datum", datum);
                        ticketDataMap.put("Problembeschreibung", problem);
                        ticketDataMap.put("StatusID", statID);
                        ticketDataMap.put("KategorieID", catID);


                        //Das einzufügende JSONObject wird mit den Daten befüllt und in die entsprechende Tabelle geladen
                        JSONObject postObject = Utils.prepareDataForPost(ticketDataMap);
                        RestUsage.postOneItem(postObject,"Ticket", CreateTicket.this);

                        //Intent backToMenu = new Intent(CreateTicket.this, MenuController.class);
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
                    Toast.makeText(CreateTicket.this, "Die Erstellung des Tickets wurde abgebrochen",
                            Toast.LENGTH_LONG);

                }
            });
        }



}


