package com.pupus.emi.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pupus.emi.model.OnJSONResponseCallback;
import com.pupus.emi.view.LayoutUtils;
import com.pupus.emi.model.RestUtils;
import com.pupus.emi.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class CreateTicketController extends AppCompatActivity {


    private EditText inputTitle;
    private EditText inputProblem;
    private Spinner spinnerStatus;
    private LinearLayout checkBoxContainer;

    private ArrayList<HashMap<String, String>> statusList;
    private HashMap<String, String> categoryMap;
    private ArrayList<CheckBox> checkers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_ticket);

        Button buttonCreate;
        Button buttonCancel;

        //Zurückpfeil unsichtbar machen, da er in dieser View keinen Sinn macht
        ImageView back_arrow = findViewById(R.id.back_arrow);
        back_arrow.setVisibility(View.INVISIBLE);

        //Text vom Titel anpassen
        TextView title = findViewById(R.id.viewCaption);
        title.setText(R.string.createTicket);

        //Intent auf das Haus setzen
        ImageView house = findViewById(R.id.home);
        house.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Nutzerausgabe, dass abgebrochen wurde
                Toast.makeText(CreateTicketController.this, "Die Erstellung des Tickets wurde abgebrochen",
                        Toast.LENGTH_LONG).show();

                Intent backToHome = new Intent(CreateTicketController.this, MenuController.class);
                startActivity(backToHome);
            }
        });

        //Buttons initialisieren
        buttonCreate = findViewById(R.id.buttonLeft);
        buttonCreate.setText(R.string.ok);
        buttonCancel = findViewById(R.id.buttonRight);
        buttonCancel.setText(R.string.reset);

        // Input Felder mit der xml-Datei verknüpft
        inputTitle = findViewById(R.id.textInputEditTextTitle);
        inputProblem = findViewById(R.id.textInputEditTextProblem);
        checkBoxContainer = findViewById(R.id.linLayCheckBoxes);
        spinnerStatus = findViewById(R.id.spinnerStatus);


        //Status aus der DB lesen und sowohl in den Spinner, als auch in eine ArrayList schreiben
        RestUtils.getAllItems("Status", CreateTicketController.this, new OnJSONResponseCallback() {
            @Override
            public void onJSONResponse(JSONArray response) {

                statusList = JSONUtils.jsonToArrayListHash(response);
                LayoutUtils.setDropDownStatusContent(spinnerStatus, CreateTicketController.this, statusList);
            }

            //Unwichtig, da man immer ein Array bekommt
            //Der Fall, dass sich das ändern könnte, sollte jedoch behandelt werden
            @Override
            public void onJSONResponse(String id) {

            }
        });

        //Kategorien aus der DB lesen und sowohl in die CheckListe, als auch in eine ArrayList schreiben
        RestUtils.getAllItems("Kategorie", CreateTicketController.this, new OnJSONResponseCallback() {
            @Override
            public void onJSONResponse(JSONArray response) {
                categoryMap = JSONUtils.jsonArraytoHashMap(response);
                checkers = LayoutUtils.setAllCategories(checkBoxContainer, CreateTicketController.this, categoryMap);
            }

            //Unwichtig, da man immer ein Array bekommt
            //Der Fall, dass sich das ändern könnte, sollte jedoch behandelt werden
            @Override
            public void onJSONResponse(String id) {

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
                } else if (inputProblem.getText().toString().equals("")) {
                    Toast.makeText(CreateTicketController.this, "Bitte geben Sie eine Problembeschreibung ein.",
                            Toast.LENGTH_LONG).show();
                } else {
                    //Lokale HashMap zur Speicherung der eingegebenen Daten
                    HashMap<String, String> ticketDataMap = LayoutUtils.getStaticContent(inputTitle, inputProblem);

                    //ausgewählten Status herausfinden und die korrespondierende StatusID speichern
                    String statID = LayoutUtils.getStatus(spinnerStatus, statusList);

                    //ausgewählte Kategorie herausfinden und die korrespondierende KategorieID speichern
                    ArrayList<String> catID = LayoutUtils.getSelectedCategories(checkers);

                    //Daten in die Hashmap schreiben
                    ticketDataMap.put("StatusID", statID);

                    //Das einzufügende JSONObject wird mit den Daten befüllt und in die entsprechende Tabelle geladen
                    JSONObject postObject = JSONUtils.prepareDataForPost(ticketDataMap);

                    //Die Kategorien müssen später hinzugefügt werden da es sich um ein JSONArray handeln muss
                    //Das wird von der API vorgegeben
                    try {
                        postObject.put("KategorieID", JSONUtils.stringArrayToJsonArray(catID));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RestUtils.postOneItem(postObject, "Ticket", CreateTicketController.this, new OnJSONResponseCallback() {
                        @Override
                        public void onJSONResponse(JSONArray response) {

                        }

                        @Override
                        public void onJSONResponse(String id) {

                            Log.e(CreateTicketController.class.getSimpleName(), "Angelegte ID: " + id);

                            Intent showTheTicket = new Intent(CreateTicketController.this, ShowTicketController.class);
                            Bundle b = new Bundle();
                            b.putInt("key", Integer.parseInt(id));
                            showTheTicket.putExtras(b);
                            startActivity(showTheTicket);


                        }
                    });

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

                checkBoxContainer.setSelected(false);

            }
        });

    }


}


