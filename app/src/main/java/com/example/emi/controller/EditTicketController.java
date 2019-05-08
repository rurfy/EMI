package com.example.emi.controller;

import android.content.Context;
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

import com.example.emi.model.OnJSONResponseCallback;
import com.example.emi.model.RestUtils;
import com.example.emi.view.LayoutUtils;
import com.example.emi.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


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
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_ticket);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            ticketId.add(0, b.getInt("key"));
        } else {
            Log.e("FehlerID", "keine ID eingetragen");
        }

        context = EditTicketController.this;

        inputTitle = findViewById(R.id.textInputEditTextTitle);
        inputProblem = findViewById(R.id.textInputEditTextProblem);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        checkBoxContainer = findViewById(R.id.linLayCheckBoxes);


        // Buttons Bezeichnung zuweisen
        buttonCreate = findViewById(R.id.buttonLeft);
        buttonCreate.setText(R.string.ok);
        buttonCancel = findViewById(R.id.buttonRight);
        buttonCancel.setText(R.string.cancel);


        RestUtils.getAllItems("Status", context, new OnJSONResponseCallback() {
            @Override
            public void onJSONResponse(JSONArray response) {

                statusList = JSONUtils.jsonToArrayListHash(response);

                LayoutUtils.setDropDownStatusContent(spinnerStatus, EditTicketController.this, statusList);


                RestUtils.getAllItems("Ticket/" + ticketId.get(0), context, new OnJSONResponseCallback() {
                    @Override
                    public void onJSONResponse(JSONArray response) {

                        HashMap<String, String> ticketDataMap = JSONUtils.jsonToArrayListHash(response).get(0);

                        LayoutUtils.setStaticContent(inputTitle, inputProblem, ticketDataMap, true);
                        LayoutUtils.setSelectedStatus(spinnerStatus, ticketDataMap, statusList);



                        RestUtils.getAllItems("Ticket_hat_Kategorie/" + ticketId.get(0) + "/TicketID", context, new OnJSONResponseCallback() {
                            @Override
                            public void onJSONResponse(JSONArray response) {

                                final ArrayList<String> selectedCategories = JSONUtils.jsonToArrayListString(response, "KategorieID");

                                RestUtils.getAllItems("Kategorie", context, new OnJSONResponseCallback() {
                                    @Override
                                    public void onJSONResponse(JSONArray response) {

                                        HashMap<String, String> categoriesHashMap = JSONUtils.jsonArraytoHashMap(response);

                                        checkBoxesCategories = LayoutUtils.setSelectedCategoriesCheckBox(checkBoxContainer, EditTicketController.this, categoriesHashMap, selectedCategories);

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
            }

            //Unwichtig, da man immer ein Array bekommt
            //Der Fall, dass sich das ändern könnte, sollte jedoch behandelt werden
            @Override
            public void onJSONResponse(String id) {

            }
        });


        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e(EditTicketController.this.getLocalClassName(), "Der OK Button wurde benutzt.");

                if (inputTitle.getText().toString().equals("")) {
                    Toast.makeText(EditTicketController.this, "Bitte geben Sie einen Titel für das Ticket ein.",
                            Toast.LENGTH_LONG).show();
                } else if (inputProblem.getText().toString().equals("")) {
                    Toast.makeText(EditTicketController.this, "Bitte geben Sie eine Problembeschreibung ein.",
                            Toast.LENGTH_LONG).show();
                } else {
                    //Lokale HashMap zur Speicherung der eingegebenen Daten
                    HashMap<String, String> ticketDataMap = LayoutUtils.getStaticContent(inputTitle, inputProblem, EditTicketController.this);

                    //ausgewählten Status herausfinden und die korrespondierende StatusID speichern
                    String statID = LayoutUtils.getStatus(spinnerStatus, statusList);

                    //ausgewählte Kategorie herausfinden und die korrespondierende KategorieID speichern
                    ArrayList<String> catID = LayoutUtils.getSelectedCategories(checkBoxesCategories);

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
                    RestUtils.postOneItem(postObject, "Ticket", EditTicketController.this, new OnJSONResponseCallback() {
                        @Override
                        public void onJSONResponse(JSONArray response) {

                        }

                        @Override
                        public void onJSONResponse(String id) {

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