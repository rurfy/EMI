package com.example.emi.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

        //Zurückpfeil unsichtbar machen, da er in dieser View keinen Sinn macht
        ImageView back_arrow = findViewById(R.id.back_arrow);
        back_arrow.setVisibility(View.INVISIBLE);

        //Text vom Titel anpassen
        TextView title = (TextView) findViewById(R.id.viewCaption);
        title.setText("");

        //Intent auf das Haus setzen
        ImageView house = findViewById(R.id.home);
        house.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToHome = new Intent(ShowTicketController.this, MenuController.class);
                startActivity(backToHome);
            }
        });

        // Auslesen der ID aus den übertragenen Parametern
        Bundle b = getIntent().getExtras();
        if (b != null) {
            Log.e("Bunlde key", Integer.toString(b.getInt("key")));
            ticketId.add(0, b.getInt("key"));


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

            // Abfragen der Daten des geforderten Tickets
            RestUtils.getAllItems("Ticket/" + ticketId.get(0), context, new OnJSONResponseCallback() {
                @Override
                public void onJSONResponse(JSONArray response) {

                    HashMap<String, String> ticketDataMap = JSONUtils.jsonToArrayListHash(response).get(0);

                    // Anzeigen von Titel, Problembeschreibung und Status
                    LayoutUtils.setStaticContent(inputTitle, inputProblem, ticketDataMap, false);
                    LayoutUtils.setStatus(textViewStatus, ticketDataMap);

                    // Anfragen der Kategorien eines Tickets (Rückgabewert KategorieID'S)
                    RestUtils.getAllItems("Ticket_hat_Kategorie/" + ticketId.get(0) + "/TicketID", context, new OnJSONResponseCallback() {
                        @Override
                        public void onJSONResponse(JSONArray response) {

                            final ArrayList <String> selectedCategories = JSONUtils.jsonToArrayListString(response, "KategorieID");

                            // Anfragen aller existierenden Kategorien (Rückgabewert KategorieID's mit zugehörigem Titel)
                            RestUtils.getAllItems("Kategorie", ShowTicketController.this, new OnJSONResponseCallback() {
                                @Override
                                public void onJSONResponse(JSONArray response) {

                                    HashMap<String, String> categoriesHashMap = JSONUtils.jsonArraytoHashMap(response);

                                    // Anzeigen der Kategorien des Tickets
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

                    // Wechseln zur Bearbeiten-View, Übergeben der TicketID
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

                    // Wechseln zur View für die Anzeige aller Tickets
                    finish();
                }
            });
        } else {

            // Wechseln zur View für die Anzeige aller Tickets, wenn keine TicketID übergeben wurde
            Toast.makeText(ShowTicketController.this, "Fehler bei der Übertragung der ID", Toast.LENGTH_LONG).show();
            Intent toAllTicketsPage = new Intent(ShowTicketController.this, AllTicketsController.class);
            b.putInt("Error", 1);
            toAllTicketsPage.putExtras(b);
            startActivity(toAllTicketsPage);
            finish();
        }
    }

}