package com.pupus.emi.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.pupus.emi.R;
import com.pupus.emi.model.OnJSONResponseCallback;
import com.pupus.emi.model.RestUtils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class AllTicketsController extends AppCompatActivity {

    private ArrayList<HashMap<String, String>> list;
    private ListView lvAllTickets;
    private ListAdapter adapter;
    private EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_tickets);

        //ListView initialisieren
        lvAllTickets = findViewById(R.id.ticketList);

        //Zurückpfeil unsichtbar machen, da er in dieser View keinen Sinn macht
        ImageView back_arrow = findViewById(R.id.back_arrow);
        back_arrow.setVisibility(View.INVISIBLE);

        //Text vom Titel anpassen
        TextView title = findViewById(R.id.viewCaption);
        title.setText(R.string.allTickets);

        //Suchleiste iniatilisieren
        search = findViewById(R.id.searchText);

        //Intent auf das Haus setzen
        ImageView house = findViewById(R.id.home);
        house.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToHome = new Intent(AllTicketsController.this, MenuController.class);
                startActivity(backToHome);
            }
        });

        //Intent auf die einzelnen List Elemente setzen und ID mitgeben
        lvAllTickets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent toShowTicket = new Intent(AllTicketsController.this, ShowTicketController.class);
                toShowTicket.putExtra("key", Integer.parseInt(Objects.requireNonNull(list.get(position).get("ID"))));
                startActivity(toShowTicket);
            }
        });

        //wird ausgeführt sobald die die XML geladen wird
        //holt sich alle Informationen aus der DB und schreibt sie entsprechend in die ListView
        RestUtils.getAllItems("Ticket", AllTicketsController.this, new OnJSONResponseCallback() {
            @Override
            public void onJSONResponse(JSONArray response) {

                //lokale Liste wird gefüllt
                list = JSONUtils.jsonToArrayListHash(response);

                //der Adapter schreibt die ID, den Titel, das Datum und den Status in "all_tickets_item.xml"
                //wenn ein Datensatz komplett in "all_tickets_item.xml" geschrieben wurde, wird dieses Item der ListView hinzugefügt
                //danach wird ein neues Item mit einem neuen Datensatz beschrieben, bis es keine Datensätze mehr gibt
                adapter = new SimpleAdapter(AllTicketsController.this, list, R.layout.all_tickets_item,
                        new String[]{"ID", "Titel", "Datum"}, new int[]{R.id.itemID, R.id.itemTitle, R.id.itemDate}) {

                    //der Text vom Status wird in ein Ampelsystem umgewandelt
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View row = super.getView(position, convertView, parent);

                        if (row == null) {
                            LayoutInflater inflater = getLayoutInflater();
                            row = inflater.inflate(R.layout.all_tickets_item, parent, false);
                        }

                        //der Status wird initailisiert
                        TextView label = row.findViewById(R.id.itemStatus);

                        //je nach ID erhält der Status eine andere Farbe
                        if (Objects.requireNonNull(list.get(position).get("StatusID")).equals("10")) {
                            label.setBackgroundResource(R.drawable.circle_red);
                        } else if (Objects.requireNonNull(list.get(position).get("StatusID")).equals("50")) {
                            label.setBackgroundResource(R.drawable.circle_yellow);
                        } else {
                            label.setBackgroundResource(R.drawable.circle_green);
                        }
                        return row;
                    }
                };
                lvAllTickets.setAdapter(adapter);

                //Errichten eines Listeners der nach dem Suchbegriff sucht
                search.addTextChangedListener(new TextWatcher() {
                    //unwichtig, da bei jeder Änderung des Textes gesucht werden soll
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    //durch die Methode onTextChanged wird sofort bei jeder Änderung des Wortes nach dem neuen Begriff gesucht
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        ((SimpleAdapter) AllTicketsController.this.adapter).getFilter().filter(search.getText().toString().toLowerCase());
                    }

                    //unwichtig, da bei jeder Änderung des Textes gesucht werden soll
                    @Override
                    public void afterTextChanged(Editable s) {

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