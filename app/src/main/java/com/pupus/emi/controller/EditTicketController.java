package com.pupus.emi.controller;

import android.content.Context;
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
import com.pupus.emi.model.RestUtils;
import com.pupus.emi.view.LayoutUtils;
import com.pupus.emi.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class EditTicketController extends AppCompatActivity {

    private EditText inputTitle;
    private EditText inputProblem;
    private Spinner spinnerStatus;
    private LinearLayout checkBoxContainer;
    private final ArrayList<Integer> ticketId = new ArrayList<>();
    private ArrayList<CheckBox> checkBoxesCategories = new ArrayList<>();
    private ArrayList<HashMap<String, String>> statusList = new ArrayList<>();
    private ArrayList<String> catID = new ArrayList<>();
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_ticket);

        Button buttonCreate;
        Button buttonCancel;

        Bundle b = getIntent().getExtras();
        if (b != null) {
            ticketId.add(0, b.getInt("key"));
        } else {
            Log.e("FehlerID", "keine ID eingetragen");
        }

        //Zurückpfeil unsichtbar machen, da er in dieser View keinen Sinn macht
        ImageView back_arrow = findViewById(R.id.back_arrow);
        back_arrow.setVisibility(View.INVISIBLE);

        //Text vom Titel anpassen
        TextView title = findViewById(R.id.viewCaption);
        title.setText(R.string.editTicket);

        //Intent auf das Haus setzen
        ImageView house = findViewById(R.id.home);
        house.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToHome = new Intent(EditTicketController.this, MenuController.class);
                startActivity(backToHome);
            }
        });

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

        // Abfragen aller existierenden Stati
        RestUtils.getAllItems("Status", context, new OnJSONResponseCallback() {
            @Override
            public void onJSONResponse(JSONArray response) {

                statusList = JSONUtils.jsonToArrayListHash(response);

                // Stati als Auswahl in die Drop-Down-Liste geben
                LayoutUtils.setDropDownStatusContent(spinnerStatus, EditTicketController.this, statusList);

                // Abfragen der Daten des geforderten Tickets
                RestUtils.getAllItems("Ticket/" + ticketId.get(0), context, new OnJSONResponseCallback() {
                    @Override
                    public void onJSONResponse(JSONArray response) {

                        HashMap<String, String> ticketDataMap = JSONUtils.jsonToArrayListHash(response).get(0);

                        // Anzeigen von Titel, Problembeschreibung und Status
                        LayoutUtils.setStaticContent(inputTitle, inputProblem, ticketDataMap, true);
                        LayoutUtils.setSelectedStatus(spinnerStatus, ticketDataMap, statusList);


                        // Anfragen aller existierenden Kategorien (Rückgabewert KategorieID's mit zugehörigem Titel)

                        RestUtils.getAllItems("Ticket_hat_Kategorie/" + ticketId.get(0) + "/TicketID", context, new OnJSONResponseCallback() {
                            @Override
                            public void onJSONResponse(JSONArray response) {

                                final ArrayList<String> selectedCategories = JSONUtils.jsonToArrayListString(response, "KategorieID");

                                // Anfragen aller existierenden Kategorien (Rückgabewert KategorieID's mit zugehörigem Titel)
                                RestUtils.getAllItems("Kategorie", context, new OnJSONResponseCallback() {
                                    @Override
                                    public void onJSONResponse(JSONArray response) {

                                        HashMap<String, String> categoriesHashMap = JSONUtils.jsonArraytoHashMap(response);

                                        // Kategorien als Checkboxen darstellen
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

                // Verhindern, dass der Nutzer die Felder Titel oder Problembeschreibung leer lässt
                if (inputTitle.getText().toString().equals("")) {
                    Toast.makeText(EditTicketController.this, "Bitte geben Sie einen Titel für das Ticket ein.",
                            Toast.LENGTH_LONG).show();
                } else if (inputProblem.getText().toString().equals("")) {
                    Toast.makeText(EditTicketController.this, "Bitte geben Sie eine Problembeschreibung ein.",
                            Toast.LENGTH_LONG).show();
                } else {
                    //Lokale HashMap zur Speicherung der eingegebenen Daten
                    HashMap<String, String> ticketDataMap = LayoutUtils.getStaticContent(inputTitle, inputProblem);

                    //ausgewählten Status herausfinden und die korrespondierende StatusID speichern
                    String statID = LayoutUtils.getStatus(spinnerStatus, statusList);

                    //ausgewählte Kategorie herausfinden und die korrespondierende KategorieID speichern
                     catID = LayoutUtils.getSelectedCategories(checkBoxesCategories);

                    //Daten in die Hashmap schreiben
                    ticketDataMap.put("StatusID", statID);


                    //Das einzufügende JSONObject wird mit den Daten befüllt (ausgenommen der Kategorien)
                    JSONObject postObject = JSONUtils.prepareDataForPost(ticketDataMap);

                    try {
                        postObject.put("KategorieID", JSONUtils.stringArrayToJsonArray(catID));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // Ticket Daten updaten
                    RestUtils.updateOneItem(postObject, "Ticket", EditTicketController.this, new OnJSONResponseCallback() {
                        @Override
                        public void onJSONResponse(JSONArray response) {

                            //Das einzufügende JSONObject wird mit den ausgewählten Kategorien befüllt
                            JSONObject postObject = new JSONObject();
                            try {
                                postObject.put("Typ", "UPDATE");
                                postObject.put("TicketID", ticketId.get(0));
                                postObject.put ("KategorieID", JSONUtils.stringArrayToJsonArray(catID));
                            } catch (JSONException e) {
                                e.printStackTrace();

                            }
                            // Updaten der Kategorien eines Tickets
                            RestUtils.updateOneItem(postObject, "Ticket_hat_Kategorie", EditTicketController.this, new OnJSONResponseCallback() {
                                @Override
                                public void onJSONResponse(JSONArray response) {

                                    // Wechseln zur View zum Anzeigen des Tickets, Übergeben der TicketID
                                    Intent toShowTicket = new Intent(EditTicketController.this, ShowTicketController.class);
                                    Bundle b = new Bundle();
                                    b.putInt("key", ticketId.get(0));
                                    toShowTicket.putExtras(b);
                                    startActivity(toShowTicket);
                                    finish();

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
            }

        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Wechseln zur View zum Anzeigen des Tickets, Übergeben der TicketID
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